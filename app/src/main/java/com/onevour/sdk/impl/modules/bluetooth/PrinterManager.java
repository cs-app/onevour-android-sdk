package com.onevour.sdk.impl.modules.bluetooth;


import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.onevour.core.utilities.commons.RefSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class PrinterManager {

    RefSession refSession = new RefSession();

    private final static String TAG = PrinterManager.class.getSimpleName();

    private Context context;

    // connection

    private BluetoothAdapter bluetoothAdapter;

    private BluetoothSocket socket;

    private BluetoothDevice device;

    // writer

    private OutputStream outputStream;

    private InputStream inputStream;

    private byte[] readBuffer;

    private int readBufferPosition;

    //private PrinterWriter writer;

    private final AtomicBoolean isConnected = new AtomicBoolean(false);

    private final AtomicBoolean isFound = new AtomicBoolean(false);

    private final AtomicBoolean stopWorker = new AtomicBoolean(false);

    // listener

    private Listener listener;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, action);
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                if (nonNull(device)) {
                    Log.d(TAG, "broadcast on receive, found device : " + device.getAddress());
                } else {
                    Log.d(TAG, "broadcast on receive, not found device ");
                }
            } else if (BluetoothDevice.ACTION_NAME_CHANGED.equals(action)) {

            } else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
                isConnected.set(false);
                clearSocketAndStream();
            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                isConnected.set(false);
                clearSocketAndStream();
            }
        }

    };

    private static PrinterManager printerManager;

    public PrinterManager() {

    }

    public static PrinterManager newInstance() {
        if (null == printerManager) printerManager = new PrinterManager();
        return printerManager;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }


    /**
     * init bluetooth connection
     * 1. clear properties bt and stream
     * 2. recreate connection if lost before
     */
    @SuppressLint("MissingPermission")
    public void initBluetoothConnection() {
        if (nowAllowPermit()) {
            Log.d(TAG, "printer not permit");
            return;
        }

        if (!isConnected.get()) reconnect();
        if (null == bluetoothAdapter) return;
        Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
        if (devices.isEmpty()) return;

        String configPrinter = refSession.findString("device_bt_address");
        isFound.set(false);
        for (BluetoothDevice device : devices) {
            if (null == device.getAddress()) continue;
            Log.d(TAG, "device name " + device.getName());
            if (device.getAddress().equals(configPrinter)) {
                this.device = device;
                isFound.set(true);
                Log.d(TAG, "device found with config : " + device.getAddress());
                break;
            } else {
                this.device = null;
                isFound.set(false);
            }
        }
        if (isFound.get()) {
            new Thread(this::establishConnection).start();
        } else {
            isConnected.set(false);
        }
    }

    @SuppressLint("MissingPermission")
    private void reconnect() {
        if (nowAllowPermit()) {
            return;
        }
        clearSocketAndStream();
        if (null != bluetoothAdapter) {
            bluetoothAdapter.cancelDiscovery();
        }
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (null == bluetoothAdapter) {
            Log.e(TAG, "Bluetooth not supported!!");
            return;
        }
//        ServerStartThread serverStart = new ServerStartThread(bluetoothAdapter);
//        serverStart.start();
        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
    }

    UUID uuid = UUID.fromString("00000000-0000-1000-8000-00805f9b34fb");

    @SuppressLint("MissingPermission")
    private void establishConnection() {
        try {
            if (device.getUuids().length > 0) {
                if (!isConnected.get()) {
                    if (nonNull(socket) && socket.isConnected()) {
                        socket.close();
                    }
                    // UUID uuid = device.getUuids()[0].getUuid();

                    if (connection(device, uuid)) {
                        outputStream = socket.getOutputStream();
                        inputStream = socket.getInputStream();
                        beginListenForData();
                        //writer = new PrinterWriter(outputStream);
                        Log.d(TAG, "connection success with device : " + device.getAddress());
                        isConnected.set(true);
                    } else isConnected.set(false);
                }
                Log.d(TAG, "connection status : " + isConnected.get());
                if (nonNull(listener)) listener.onWriterReady();

            } else {
                isConnected.set(false);
                Log.w(TAG, "uuid bt is 0, or not found");
            }
        } catch (NullPointerException e) {
            Log.e(TAG, "null pointer  " + e.getMessage());
            //FirebaseCrashlytics.getInstance().recordException(e);
        } catch (IOException e) {
            try {
                if (null != socket) socket.close();
            } catch (IOException eClose) {
                //FirebaseCrashlytics.getInstance().recordException(eClose);
                Log.e(TAG, "close connection error " + eClose.getMessage());
            } finally {
                outputStream = null;
                inputStream = null;
                //writer = null;
                socket = null;
                isConnected.set(false);
            }
            Log.e(TAG, "create connection error  " + e.getMessage());
        }
    }

    @SuppressLint("MissingPermission")
    private boolean connection(BluetoothDevice device, UUID uuid) {
        try {
            socket = device.createRfcommSocketToServiceRecord(uuid);
            socket.connect();
            return true;
        } catch (IOException e) {
            if (Objects.requireNonNull(e.getMessage()).contains("closed or timeout")) {
                return connectionWithHiddenApi(device);
            }
            outputStream = null;
            inputStream = null;
            //writer = null;
            socket = null;
            //FirebaseCrashlytics.getInstance().recordException(e);
            return false;
        }
    }

    @SuppressLint("MissingPermission")
    private boolean connectionWithHiddenApi(BluetoothDevice device) {
        try {
            socket = (BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(device, 1);
            assert socket != null;
            socket.connect();
            return true;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException |
                 IOException e) {
            outputStream = null;
            inputStream = null;
            //writer = null;
            socket = null;

            return false;
        }
    }

    /**
     * proceed discovery
     */
    public void proceedDiscovery() {
        if (nowAllowPermit()) return;
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_NAME_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        context.registerReceiver(receiver, filter);
    }

    private boolean nowAllowPermit() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "not permit bluetooth");
            message("Bluetooth not permit");
            return true;
        }
        // android >= 12
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "not permit bluetooth scan");
                message("Bluetooth scan not permit");
                return true;
            }
        }
        return false;
    }

    public void stopDiscovery() {
        context.unregisterReceiver(receiver);
    }

    /**
     * clear socket and stream
     */
    private void clearSocketAndStream() {
        try {
            if (nonNull(outputStream)) {
                outputStream.close();
                outputStream = null;
            }
            if (nonNull(inputStream)) {
                inputStream.close();
                inputStream = null;
            }
            if (nonNull(socket) && socket.isConnected()) {
                socket.close();
                socket = null;
            }
            stopWorker.set(true);
        } catch (Exception e) {
            Log.e(TAG, "stop bt " + e.getMessage());
        } finally {
            isConnected.set(false);
        }
    }

    /**
     * listen data for stream
     */
    private void beginListenForData() {
        try {
            final byte delimiter = 10;
            stopWorker.set(false);
            readBufferPosition = 0;
            readBuffer = new byte[1024];
            Thread workerThread = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted() && !stopWorker.get()) {
                    if (nonNull(inputStream)) {
                        try {
                            int bytesAvailable = inputStream.available();
                            if (bytesAvailable > 0) {
                                byte[] packetBytes = new byte[bytesAvailable];
                                inputStream.read(packetBytes);
                                for (int i = 0; i < bytesAvailable; i++) {
                                    byte b = packetBytes[i];
                                    if (b == delimiter) {
                                        byte[] encodedBytes = new byte[readBufferPosition];
                                        System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                        final String data = new String(encodedBytes, "US-ASCII");
                                        readBufferPosition = 0;
                                    } else {
                                        readBuffer[readBufferPosition++] = b;
                                    }
                                }
                            }
                        } catch (NullPointerException ex) {
                            Log.e(TAG, "start listen data null pointer, " + ex.getMessage());
                            stopWorker.set(true);
                        } catch (IOException ex) {
                            Log.e(TAG, "start listen data, " + ex.getMessage());
                            stopWorker.set(true);
                        }
                    }
                }
            });
            workerThread.start();
        } catch (Exception e) {
            Log.e(TAG, "start listen data, " + e.getMessage());
        }
    }

    private boolean nonNull(Object o) {
        return null != o;
    }

    private void message(String value) {
        Toast.makeText(context, value, Toast.LENGTH_LONG).show();
    }


    private int mState;
    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device


    @SuppressLint("MissingPermission")
    private class ServerStartThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public ServerStartThread(BluetoothAdapter bluetoothAdapter) {
            BluetoothServerSocket tmp = null;
            try {
                tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord("ONVBTCHAT", uuid);
            } catch (IOException e) {
                Log.d(TAG, "service start error", e);
            }
            this.mmServerSocket = tmp;
        }


        public void run() {
            setName("AcceptThread");
            BluetoothSocket socket = null;
            // Listen to the server socket if we're not connected
            while (mState != STATE_CONNECTED) {
                try {
                    // This is a blocking call and will only return on a
                    // successful connection or an exception
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    Log.e(TAG, "Socket Type: accept() failed", e);
                    break;
                }
                // If a connection was accepted
                if (socket != null) {
                    synchronized (PrinterManager.this) {
                        switch (mState) {
                            case STATE_LISTEN:
                            case STATE_CONNECTING:
                                // Situation normal. Start the connected thread.
                                // connected(socket, socket.getRemoteDevice(), mSocketType);
                                break;
                            case STATE_NONE:
                            case STATE_CONNECTED:
                                // Either not ready or already connected. Terminate new socket.
                                try {
                                    socket.close();
                                } catch (IOException e) {
                                    Log.e(TAG, "Could not close unwanted socket", e);
                                }
                                break;
                        }
                    }
                }
            }
            // if (D) Log.i(TAG, "END mAcceptThread, socket Type: " + mSocketType);
        }

        public void cancel() {
            //if (D) Log.d(TAG, "Socket Type" + mSocketType + "cancel " + this);
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                // Log.e(TAG, "Socket Type" + mSocketType + "close() of server failed", e);
            }
        }
    }

    public interface Listener {

        void onWriterReady();

    }

}
