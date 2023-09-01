package com.onevour.sdk.impl.modules.bluetooth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.onevour.core.utilities.commons.RefSession;
import com.onevour.core.utilities.commons.ValueOf;
import com.onevour.core.utilities.helper.UIHelper;
import com.onevour.core.utilities.json.gson.GsonHelper;
import com.onevour.sdk.impl.databinding.ActivityBluetoothBinding;
import com.onevour.sdk.impl.modules.bluetooth.components.AdapterMessage;
import com.onevour.sdk.impl.modules.bluetooth.services.v1.BluetoothSDKListener;
import com.onevour.sdk.impl.modules.bluetooth.services.v1.BluetoothSDKListenerHelper;
import com.onevour.sdk.impl.modules.bluetooth.services.v1.BluetoothSDKService;
import com.onevour.sdk.impl.modules.form.controllers.DeeplinkResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

// https://stackoverflow.com/questions/65636725/bluetooth-le-send-string-data-between-android-devices-via-application
// https://github.com/halilozel1903/AndroidBluetoothChatApp
// https://github.com/android/connectivity-samples/
// https://github.com/glodanif/BluetoothChat
// https://proandroiddev.com/android-bluetooth-as-a-service-c39c3d732e56
// https://github.com/iandouglas96/drivR/blob/master/app/src/main/java/com/pxlweavr/drivr/BluetoothService.java
// https://github.com/AndroidCrypto/BleServerBlessedOriginal
// https://www.programcreek.com/java-api-examples/?code=zeevy%2Fgrblcontroller%2Fgrblcontroller-master%2Fapp%2Fsrc%2Fmain%2Fjava%2Fin%2Fco%2Fgorest%2Fgrblcontroller%2Fservice%2FGrblBluetoothSerialService.java
// https://github.com/zeevy/grblcontroller
// https://www.c-sharpcorner.com/UploadFile/0e8478/connecting-devices-as-client-and-server-architecture-in-andr/
// https://github.com/android/connectivity-samples/tree/master/BluetoothChat

public class BluetoothActivity extends AppCompatActivity {

    private static String TAG = BluetoothActivity.class.getSimpleName();

    private final RefSession refSession = new RefSession();

    private ActivityBluetoothBinding binding;

    private BluetoothSDKService bluetoothSDKService;

    private final UUID uuid = UUID.fromString("00000000-0000-1000-8000-00805f9b34fb");

    private final AdapterMessage messages = new AdapterMessage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBluetoothBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        UIHelper.initRecyclerView(binding.rvMessage, messages);
        isGrantPermission(this);
        infoDevice();
        binding.stop.setOnClickListener(v -> {
            startActivity(new Intent(this, BluetoothDiscoveryActivity.class));
            // stopService(new Intent(this, BluetoothSDKService.class));
            //pm.connectToServer();
        });

        binding.start.setOnClickListener(v -> {
            bluetoothSDKService.connectToServer();
        });


        binding.send.setOnClickListener(v -> {
            String message = binding.message.getText().toString();
            bluetoothSDKService.write(message);
            binding.message.setText(null);
            scrollToBottom(binding.rvMessage);
        });
        //

        bindBluetoothService();
        // Register Listener
        // binding.connectionStatus.setText(bluetoothSDKService.getConnectionStatus());
        BluetoothSDKListenerHelper.registerBluetoothSDKListener(getApplicationContext(), mBluetoothListener);
    }

    private void scrollToBottom(RecyclerView recyclerView) {
        // scroll to last item to get the view of last item
        final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        final RecyclerView.Adapter adapter = recyclerView.getAdapter();
        final int lastItemPosition = adapter.getItemCount() - 1;

        layoutManager.scrollToPositionWithOffset(lastItemPosition, 0);
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                // then scroll to specific offset
                View target = layoutManager.findViewByPosition(lastItemPosition);
                if (target != null) {
                    int offset = recyclerView.getMeasuredHeight() - target.getMeasuredHeight();
                    layoutManager.scrollToPositionWithOffset(lastItemPosition, offset);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isGrantPermission(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        infoDevice();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(getApplicationContext(), mBluetoothListener);
    }

    /**
     * Bind Bluetooth Service
     */
    private void bindBluetoothService() {
        // Bind to LocalService
        Intent intent = new Intent(getApplicationContext(), BluetoothSDKService.class);
        getApplicationContext().bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    /**
     * Handle service connection
     */
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            BluetoothSDKService.LocalBinder binder = (BluetoothSDKService.LocalBinder) service;
            bluetoothSDKService = binder.getService();
            binding.connectionStatus.setText(bluetoothSDKService.getConnectionStatus());
            binding.start.setEnabled(0 == bluetoothSDKService.getConnectionStatusCode());
            Log.d(TAG, "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            // Implement your logic here
            Log.d(TAG, "onServiceDisconnected(ComponentName componentName)");
            bluetoothSDKService = null;
        }
    };


    @SuppressLint("MissingPermission")
    private BluetoothSDKListener mBluetoothListener = new BluetoothSDKListener() {

        @Override
        public void onDiscoveryStarted() {
            // Implement your logic here
            Log.d(TAG, "onDiscoveryStarted");
            messages.addMore("discover start");
            scrollToBottom(binding.rvMessage);
        }

        @Override
        public void onDiscoveryStopped() {
            // Implement your logic here
            Log.d(TAG, "onDiscoveryStopped");
            messages.addMore("discover stop");
            scrollToBottom(binding.rvMessage);
        }

        @Override
        public void onDeviceDiscovered(BluetoothDevice device) {
            // Implement your logic here
            Log.d(TAG, "onDeviceDiscovered(BluetoothDevice device)");
            messages.addMore(device.getName() + " discover");
            scrollToBottom(binding.rvMessage);
        }

        @Override
        public void onDeviceConnected(BluetoothDevice device) {
            // Do stuff when connected
            Log.d(TAG, "onDeviceConnected(BluetoothDevice device)");
            if (Objects.isNull(device)) {
                messages.addMore("connected as server");
            } else {
                messages.addMore("connected as client");
            }
            scrollToBottom(binding.rvMessage);
            binding.connectionStatus.setText(bluetoothSDKService.getConnectionStatus());
            binding.start.setEnabled(0 == bluetoothSDKService.getConnectionStatusCode());
        }

        StringBuilder sb = new StringBuilder();
        boolean isJson = false;


        @Override
        public void onMessageReceived(BluetoothDevice device, String message) {
            // Implement your logic here
            Log.d(TAG, "onMessageReceived(BluetoothDevice device, String message)");
            Log.d(TAG, "receive message on listener " + message);
            String messageTrim = message.trim();
            if (messageTrim.startsWith("{")) {
                isJson = true;
                sb.setLength(0);
                sb.append(messageTrim);
            } else if (messageTrim.endsWith("}")) {
                sb.append(messageTrim);
                DeeplinkResult deeplinkResult = GsonHelper.gson.fromJson(sb.toString(), DeeplinkResult.class);
                Gson gson = new GsonBuilder()
                        .serializeNulls()
                        .setPrettyPrinting()
                        .create();
                deeplinkResult.setFace(null);
                deeplinkResult.setSignature(null);
                deeplinkResult.setFp(null);
                messages.addMore(gson.toJson(deeplinkResult));
                isJson = false;
            } else {
                if (isJson) {
                    sb.append(messageTrim);
                } else {
                    messages.addMore(device.getName() + "\n" + message);
                }

            }

            scrollToBottom(binding.rvMessage);
        }

        @Override
        public void onMessageSent(BluetoothDevice device) {
            // Implement your logic here
            Log.d(TAG, "onMessageSent(BluetoothDevice device)");
            messages.addMore("message sent");
            scrollToBottom(binding.rvMessage);
        }

        @Override
        public void onError(String message) {
            // Implement your logic here
            Log.d(TAG, "ui " + message);
            messages.addMore("error " + message);
            binding.connectionStatus.setText(bluetoothSDKService.getConnectionStatus());
            binding.start.setEnabled(0 == bluetoothSDKService.getConnectionStatusCode());
            scrollToBottom(binding.rvMessage);
        }

        @Override
        public void onDeviceDisconnected() {
            Log.d(TAG, "onDeviceDisconnected()");
            messages.addMore("device disconnected");
            binding.start.setEnabled(0 == bluetoothSDKService.getConnectionStatusCode());
            scrollToBottom(binding.rvMessage);
        }

    };


    private void infoDevice() {
        String deviceName = refSession.findString("device_bt_name");
        String deviceAddress = refSession.findString("device_bt_address");
        binding.connectionStatus.setText(null);
        if (ValueOf.isNull(deviceAddress)) {
            binding.device.setText("No device selected");
        } else {
            binding.device.setText(deviceName);
            binding.device.append(" (");
            binding.device.append(deviceAddress);
            binding.device.append(")");
        }
    }

    private void discovery(View view) {
        startActivity(new Intent(this, BluetoothDiscoveryActivity.class));
    }


    private void isGrantPermission(Context context) {
        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.BLUETOOTH);
        // android >= 12
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissions.add(Manifest.permission.BLUETOOTH_SCAN);
            permissions.add(Manifest.permission.BLUETOOTH_ADVERTISE);
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT);
        }

        List<String> requestList = new ArrayList<>();
        for (String s : permissions) {
            if (ActivityCompat.checkSelfPermission(context, s) == PackageManager.PERMISSION_GRANTED) {
                continue;
            }
            if (context instanceof Activity) {
                ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, s);
                requestList.add(s);
            }
        }
        if (requestList.isEmpty()) {
            Log.d(TAG, "permission grant");
            return;
        }
        ActivityCompat.requestPermissions((Activity) context, requestList.toArray(new String[0]), 100);
        Log.d(TAG, "permission not grant : ");
    }

    private static void askToUser(Activity activity, boolean result, List<String> permissions) {
        if (result) return;
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                ActivityCompat.requestPermissions(activity, new String[]{permission}, 100);
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{permission}, 100);
            }
        }
    }

}