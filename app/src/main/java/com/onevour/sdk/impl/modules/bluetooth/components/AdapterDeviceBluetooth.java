package com.onevour.sdk.impl.modules.bluetooth.components;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.view.View;

import com.onevour.core.components.recycleview.AdapterGenericBasic;
import com.onevour.core.components.recycleview.HolderGenericBasic;
import com.onevour.sdk.impl.databinding.AdapterDeviceBluetoothBinding;

import java.util.Objects;

@SuppressLint("MissingPermission")
public class AdapterDeviceBluetooth extends AdapterGenericBasic<AdapterDeviceBluetooth.HolderDeviceBluetooth, BluetoothDevice> {

    public static class HolderDeviceBluetooth extends HolderGenericBasic<AdapterDeviceBluetoothBinding, BluetoothDevice> {

        public HolderDeviceBluetooth(AdapterDeviceBluetoothBinding binding) {
            super(binding);
            binding.getRoot().setOnClickListener(this::selectedBluetoothDevice);
        }

        private void selectedBluetoothDevice(View view) {
            HolderDeviceBluetoothListener listener = getListener(HolderDeviceBluetoothListener.class);
            if (Objects.isNull(listener)) return;
            listener.onSelectedBluetoothDevice(value);
        }

        @Override
        protected void onBindViewHolder(BluetoothDevice o) {
            binding.name.setText(o.getName());
            binding.id.setText(o.getAddress());
        }
    }

    public static interface HolderDeviceBluetoothListener extends HolderGenericBasic.Listener {

        void onSelectedBluetoothDevice(BluetoothDevice value);
    }

}
