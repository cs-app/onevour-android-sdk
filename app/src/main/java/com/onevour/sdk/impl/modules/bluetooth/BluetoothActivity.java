package com.onevour.sdk.impl.modules.bluetooth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.onevour.core.utilities.commons.RefSession;
import com.onevour.core.utilities.commons.ValueOf;
import com.onevour.sdk.impl.R;
import com.onevour.sdk.impl.databinding.ActivityBluetoothBinding;
import com.onevour.sdk.impl.modules.bluetooth.services.PrinterService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// https://stackoverflow.com/questions/65636725/bluetooth-le-send-string-data-between-android-devices-via-application
// https://github.com/halilozel1903/AndroidBluetoothChatApp
// https://github.com/android/connectivity-samples/
// https://github.com/glodanif/BluetoothChat
public class BluetoothActivity extends AppCompatActivity {

    private static String TAG = BluetoothActivity.class.getSimpleName();

    private final RefSession refSession = new RefSession();

    private ActivityBluetoothBinding binding;

    UUID uuid = UUID.fromString("00000000-0000-1000-8000-00805f9b34fb");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBluetoothBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        isGrantPermission(this);
        binding.scan.setOnClickListener(this::discovery);
        infoDevice();
        binding.connect.setOnClickListener(v -> {
            PrinterManager pm = PrinterManager.newInstance();
            pm.setContext(this);
            pm.initBluetoothConnection();
//            Intent intent = new Intent(this, PrinterService.class);
//            intent.setAction("PRINT-FAKTUR-ALL");
//            startService(intent);
        });
        binding.server.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                try {
                    bluetoothAdapter.listenUsingRfcommWithServiceRecord("ONVBTCHAT", uuid);
                    binding.server.setText("Server Start");
                } catch (IOException e) {
                    binding.server.setText("Server Error");
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        infoDevice();
    }

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

    @Override
    protected void onResume() {
        super.onResume();
        isGrantPermission(this);
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