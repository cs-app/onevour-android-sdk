package com.onevour.sdk.impl.modules.bluetooth.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.onevour.sdk.impl.modules.bluetooth.PrinterManager;


public class PrinterService extends IntentService {

    private static final String TAG = PrinterService.class.getSimpleName();

    private PrinterManager pm;


    public PrinterService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        pm = PrinterManager.newInstance();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d(TAG, "onTaskRemoved");
        super.onTaskRemoved(rootIntent);
    }

    @Override
    protected void onHandleIntent(@Nullable final Intent intent) {
        Log.d(TAG, "onHandleIntent");
        if (null == intent) return;
        if (null == pm) pm = PrinterManager.newInstance();
        pm.setListener(() -> {
            Log.d(TAG, "write " + intent.getAction());
            //print(intent);
        });
        pm.initBluetoothConnection();
    }

    private void print(Intent intent) {
//        if (null == writer) return;
//        if ("PRINT-TEST".equalsIgnoreCase(intent.getAction())) {
//            writer.createSample();
//        }
//        if ("PRINT-FAKTUR-ALL".equalsIgnoreCase(intent.getAction())) {
//            Customer customer = presenter.customer();
//            writer.printFaktur(CustomerUtils.id(customer));
//            return;
//        }
//        if ("PRINT-FAKTUR-SALES-MASTER".equalsIgnoreCase(intent.getAction())) {
//            String smsId = intent.getStringExtra("SMSTR");
//            if (null == smsId || smsId.trim().isEmpty()) return;
//            writer.printFakturByNoSales(smsId);
//        }
    }

}
