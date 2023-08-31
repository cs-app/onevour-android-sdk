package com.onevour.sdk.impl.modules.bluetooth.services.v1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.bluetooth.BluetoothDevice;

import java.util.Objects;

public class BluetoothSDKListenerHelper {

    private static BluetoothSDKBroadcastReceiver mBluetoothSDKBroadcastReceiver;

    public static class BluetoothSDKBroadcastReceiver extends BroadcastReceiver {
        private BluetoothSDKListener mGlobalListener;

        public void setBluetoothSDKListener(BluetoothSDKListener listener) {
            mGlobalListener = listener;
        }

        public boolean removeBluetoothSDKListener(BluetoothSDKListener listener) {
            if (mGlobalListener == listener) {
                mGlobalListener = null;
            }
            return mGlobalListener == null;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothUtils.EXTRA_DEVICE);
            String message = intent.getStringExtra(BluetoothUtils.EXTRA_MESSAGE);

            switch (intent.getAction()) {
                case BluetoothUtils.ACTION_DEVICE_FOUND:
                    mGlobalListener.onDeviceDiscovered(device);
                    break;
                case BluetoothUtils.ACTION_DISCOVERY_STARTED:
                    mGlobalListener.onDiscoveryStarted();
                    break;
                case BluetoothUtils.ACTION_DISCOVERY_STOPPED:
                    mGlobalListener.onDiscoveryStopped();
                    break;
                case BluetoothUtils.ACTION_DEVICE_CONNECTED:
                    mGlobalListener.onDeviceConnected(device);
                    break;
                case BluetoothUtils.ACTION_MESSAGE_RECEIVED:
                    mGlobalListener.onMessageReceived(device, message);
                    break;
                case BluetoothUtils.ACTION_MESSAGE_SENT:
                    mGlobalListener.onMessageSent(device);
                    break;
                case BluetoothUtils.ACTION_CONNECTION_ERROR:
                    mGlobalListener.onError(message);
                    break;
                case BluetoothUtils.ACTION_DEVICE_DISCONNECTED:
                    mGlobalListener.onDeviceDisconnected();
                    break;
            }
        }
    }

    public static void registerBluetoothSDKListener(Context context, BluetoothSDKListener listener) {
        if (Objects.isNull(mBluetoothSDKBroadcastReceiver)) {
            mBluetoothSDKBroadcastReceiver = new BluetoothSDKBroadcastReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BluetoothUtils.ACTION_DEVICE_FOUND);
            intentFilter.addAction(BluetoothUtils.ACTION_DISCOVERY_STARTED);
            intentFilter.addAction(BluetoothUtils.ACTION_DISCOVERY_STOPPED);
            intentFilter.addAction(BluetoothUtils.ACTION_DEVICE_CONNECTED);
            intentFilter.addAction(BluetoothUtils.ACTION_MESSAGE_RECEIVED);
            intentFilter.addAction(BluetoothUtils.ACTION_MESSAGE_SENT);
            intentFilter.addAction(BluetoothUtils.ACTION_CONNECTION_ERROR);
            intentFilter.addAction(BluetoothUtils.ACTION_DEVICE_DISCONNECTED);
            LocalBroadcastManager.getInstance(context).registerReceiver(mBluetoothSDKBroadcastReceiver, intentFilter);
        }

        mBluetoothSDKBroadcastReceiver.setBluetoothSDKListener(listener);
    }

    public static void unregisterBluetoothSDKListener(Context context, BluetoothSDKListener listener) {
        if (Objects.nonNull(mBluetoothSDKBroadcastReceiver)) {
            boolean empty = mBluetoothSDKBroadcastReceiver.removeBluetoothSDKListener(listener);
            if (empty) {
                LocalBroadcastManager.getInstance(context).unregisterReceiver(mBluetoothSDKBroadcastReceiver);
                mBluetoothSDKBroadcastReceiver = null;
            }
        }
    }
}
