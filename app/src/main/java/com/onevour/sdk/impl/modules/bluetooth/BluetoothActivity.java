package com.onevour.sdk.impl.modules.bluetooth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
import android.widget.Toast;

import com.onevour.core.utilities.commons.RefSession;
import com.onevour.core.utilities.commons.ValueOf;
import com.onevour.sdk.impl.databinding.ActivityBluetoothBinding;
import com.onevour.sdk.impl.modules.bluetooth.services.v1.BluetoothSDKListener;
import com.onevour.sdk.impl.modules.bluetooth.services.v1.BluetoothSDKListenerHelper;
import com.onevour.sdk.impl.modules.bluetooth.services.v1.BluetoothSDKService;

import java.util.ArrayList;
import java.util.List;
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

public class BluetoothActivity extends AppCompatActivity {

    private static String TAG = BluetoothActivity.class.getSimpleName();

    private final RefSession refSession = new RefSession();

    private ActivityBluetoothBinding binding;

    //
    private BluetoothSDKService mService;

    private PrinterManager pm = PrinterManager.newInstance();

    private final UUID uuid = UUID.fromString("00000000-0000-1000-8000-00805f9b34fb");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBluetoothBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        isGrantPermission(this);
        infoDevice();
        binding.stop.setOnClickListener(v -> {
            // stopService(new Intent(this, BluetoothSDKService.class));
            //pm.connectToServer();
        });

        binding.start.setOnClickListener(v -> {
            // pm.startServer();
        });


        binding.send.setOnClickListener(v -> {
            // PrinterManager pm = PrinterManager.newInstance();
            // pm.setContext(this);
            // pm.initBluetoothConnection();
            // startService(new Intent(this, BluetoothSDKService.class));
            if (pm.isConnected()) {
                String message = binding.message.getText().toString();
                pm.sendMessage(message);
                binding.message.setText(null);
            } else {
                Toast.makeText(this, "tryconnect to server", Toast.LENGTH_SHORT).show();
                pm.connectToServer();
            }
        });
        binding.server.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {

//                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//                try {
//                    bluetoothAdapter.listenUsingRfcommWithServiceRecord("ONVBTCHAT", uuid);
//                    binding.server.setText("Server Start");
//                } catch (IOException e) {
//                    binding.server.setText("Server Error");
//                    throw new RuntimeException(e);
//                }

            }
        });
        binding.server.setVisibility(View.GONE);
        //

        // bindBluetoothService();
        // Register Listener
        // BluetoothSDKListenerHelper.registerBluetoothSDKListener(getApplicationContext(), mBluetoothListener);
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
        // BluetoothSDKListenerHelper.unregisterBluetoothSDKListener(getApplicationContext(), mBluetoothListener);
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
            mService = binder.getService();
            Log.d(TAG, "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            // Implement your logic here
            Log.d(TAG, "onServiceDisconnected(ComponentName componentName)");
            mService = null;
        }
    };


    private BluetoothSDKListener mBluetoothListener = new BluetoothSDKListener() {

        @Override
        public void onDiscoveryStarted() {
            // Implement your logic here
            Log.d(TAG, "onDiscoveryStarted");
        }

        @Override
        public void onDiscoveryStopped() {
            // Implement your logic here
            Log.d(TAG, "onDiscoveryStopped");
        }

        @Override
        public void onDeviceDiscovered(BluetoothDevice device) {
            // Implement your logic here
            Log.d(TAG, "onDeviceDiscovered(BluetoothDevice device)");
        }

        @Override
        public void onDeviceConnected(BluetoothDevice device) {
            // Do stuff when connected
            Log.d(TAG, "onDeviceConnected(BluetoothDevice device)");
        }

        @Override
        public void onMessageReceived(BluetoothDevice device, String message) {
            // Implement your logic here
            Log.d(TAG, "onMessageReceived(BluetoothDevice device, String message)");
        }

        @Override
        public void onMessageSent(BluetoothDevice device) {
            // Implement your logic here
            Log.d(TAG, "onMessageSent(BluetoothDevice device)");
        }

        @Override
        public void onError(String message) {
            // Implement your logic here
            Log.d(TAG, "onError(String message)");
        }

        @Override
        public void onDeviceDisconnected() {
            Log.d(TAG, "onDeviceDisconnected()");
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