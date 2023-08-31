package com.onevour.sdk.impl.modules.bluetooth.services.v1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BluetoothSDKListenerHelper {

    private static BluetoothSDKBroadcastReceiver mBluetoothSDKBroadcastReceiver;

    public static class BluetoothSDKBroadcastReceiver extends BroadcastReceiver {

        private List<BluetoothSDKListener> mGlobalListeners = new ArrayList<>();

        public void setBluetoothSDKListener(BluetoothSDKListener listener) {
            mGlobalListeners.add(listener);
//            mGlobalListener = listener;
        }

        public boolean removeBluetoothSDKListener(BluetoothSDKListener listener) {
            return mGlobalListeners.remove(listener);
//            if (mGlobalListener == listener) {
//                mGlobalListener = null;
//            }
//            return mGlobalListener == null;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothUtils.EXTRA_DEVICE);
            String message = intent.getStringExtra(BluetoothUtils.EXTRA_MESSAGE);

            switch (intent.getAction()) {
                case BluetoothUtils.ACTION_DEVICE_FOUND:
                    for (BluetoothSDKListener mGlobalListener : mGlobalListeners)
                        mGlobalListener.onDeviceDiscovered(device);
                    break;
                case BluetoothUtils.ACTION_DISCOVERY_STARTED:
                    for (BluetoothSDKListener mGlobalListener : mGlobalListeners)
                        mGlobalListener.onDiscoveryStarted();
                    break;
                case BluetoothUtils.ACTION_DISCOVERY_STOPPED:
                    for (BluetoothSDKListener mGlobalListener : mGlobalListeners)
                        mGlobalListener.onDiscoveryStopped();
                    break;
                case BluetoothUtils.ACTION_DEVICE_CONNECTED:
                    for (BluetoothSDKListener mGlobalListener : mGlobalListeners)
                        mGlobalListener.onDeviceConnected(device);
                    break;
                case BluetoothUtils.ACTION_MESSAGE_RECEIVED:
                    for (BluetoothSDKListener mGlobalListener : mGlobalListeners)
                        mGlobalListener.onMessageReceived(device, message);
                    break;
                case BluetoothUtils.ACTION_MESSAGE_SENT:
                    for (BluetoothSDKListener mGlobalListener : mGlobalListeners)
                        mGlobalListener.onMessageSent(device);
                    break;
                case BluetoothUtils.ACTION_CONNECTION_ERROR:
                    for (BluetoothSDKListener mGlobalListener : mGlobalListeners)
                        mGlobalListener.onError(message);
                    break;
                case BluetoothUtils.ACTION_DEVICE_DISCONNECTED:
                    for (BluetoothSDKListener mGlobalListener : mGlobalListeners)
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
