package com.onevour.sdk.impl.modules.bluetooth.services.v1;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import android.os.Binder;
import android.util.Log;

// https://sahityakumarsuman.medium.com/android-service-communication-with-activity-2c01e537ab03
public class BluetoothSDKService extends Service {

    private final LocalBinder binder = new LocalBinder();

    private final UUID uuid = UUID.fromString("00000000-0000-1000-8000-00805f9b34fb");

    private Service Binder;

    // Bluetooth stuff
    private BluetoothAdapter bluetoothAdapter;

    private Set<BluetoothDevice> pairedDevices;

    private BluetoothDevice connectedDevice;

    private final int RESULT_INTENT = 15;

    // Bluetooth connections
    private ConnectThread connectThread;

    private ConnectedThread connectedThread;

    private AcceptThread acceptThread;

    private String TAG = BluetoothSDKService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "service created");
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    /**
     * Broadcast Receiver for catching ACTION_FOUND aka new device discovered
     */
    private final BroadcastReceiver discoveryBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            /*
              Our broadcast receiver for managing Bluetooth actions
            */
        }
    };

    private synchronized void startConnectedThread(BluetoothSocket bluetoothSocket) {
        connectedThread = new ConnectedThread(bluetoothSocket);
        connectedThread.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(discoveryBroadcastReceiver);
        } catch (Exception e) {
            // already unregistered
        }
        Log.d(TAG, "service destroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private void pushBroadcastMessage(String action, BluetoothDevice device, String message) {
        Intent intent = new Intent(action);
        if (Objects.nonNull(device)) {
            intent.putExtra(BluetoothUtils.EXTRA_DEVICE, device);
        }
        if (Objects.nonNull(message)) {
            intent.putExtra(BluetoothUtils.EXTRA_MESSAGE, message);
        }
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    private class AcceptThread extends Thread {

        // Body
    }

    private class ConnectThread extends Thread {

        public ConnectThread(BluetoothDevice device) {
            // Body
        }
    }

    private class ConnectedThread extends Thread {

        private final BluetoothSocket mmSocket;

        private final InputStream mmInStream;

        private final OutputStream mmOutStream;

        private final byte[] mmBuffer = new byte[1024]; // mmBuffer store for the stream

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                pushBroadcastMessage(BluetoothUtils.ACTION_CONNECTION_ERROR, null, "Error getting streams");
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        @Override
        public void run() {
            int numBytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                try {
                    // Read from the InputStream.
                    numBytes = mmInStream.read(mmBuffer);

                    String message = new String(mmBuffer, 0, numBytes);

                    // Send to broadcast the message
                    pushBroadcastMessage(BluetoothUtils.ACTION_MESSAGE_RECEIVED, mmSocket.getRemoteDevice(), message);
                } catch (IOException e) {
                    pushBroadcastMessage(BluetoothUtils.ACTION_CONNECTION_ERROR, null, "Input stream was disconnected");
                    break;
                }
            }
        }

        // Call this from the main activity to send data to the remote device.
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
                // Send to broadcast the message
                pushBroadcastMessage(BluetoothUtils.ACTION_MESSAGE_SENT, mmSocket.getRemoteDevice(), null);
            } catch (IOException e) {
                pushBroadcastMessage(BluetoothUtils.ACTION_CONNECTION_ERROR, null, "Error occurred when sending data");
            }
        }

        // Call this method from the main activity to shut down the connection.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                pushBroadcastMessage(BluetoothUtils.ACTION_CONNECTION_ERROR, null, "Could not close the connect socket");
            }
        }

        // Body
    }

    /**
     * Class used for the client Binder.
     */
    @SuppressLint("MissingPermission")
    public class LocalBinder extends Binder {


        /**
         * Enable the discovery, registering a BroadcastReceiver {@link discoveryBroadcastReceiver}
         * The discovery is filtered by LABELER_SERVER_TOKEN_NAME
         */

        public void startDiscovery(Context context) {
            IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
            registerReceiver(discoveryBroadcastReceiver, filter);
            bluetoothAdapter.startDiscovery();
            pushBroadcastMessage(BluetoothUtils.ACTION_DISCOVERY_STARTED, null, null);
        }

        /**
         * Stop discovery
         */
        public void stopDiscovery() {
            bluetoothAdapter.cancelDiscovery();
            pushBroadcastMessage(BluetoothUtils.ACTION_DISCOVERY_STOPPED, null, null);
        }

        public BluetoothSDKService getService() {
            return BluetoothSDKService.this;
        }

    }

}