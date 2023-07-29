package com.onevour.core.utilities.commons;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtils {

    private static final String TAG = PermissionUtils.class.getSimpleName();

    public static int requestCode = 501;

    private static final String[] permissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static boolean isGranted(Context context) {
        for (String s : permissions) {
            if (ContextCompat.checkSelfPermission(context, s) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static boolean isGrantPermission(Context context) {
        List<String> requestList = new ArrayList<>();
        for (String s : permissions) {
            if (ContextCompat.checkSelfPermission(context, s) != PackageManager.PERMISSION_GRANTED) {
                requestList.add(s);
            }
        }
        boolean result = requestList.size() == 0;
        if (context instanceof Activity) {
            if (!result) {
                askToUser((Activity) context, result, requestList);
            }
        }
        return result;
    }

    public static boolean isGrantPermission(Activity activity) {
        List<String> requestList = new ArrayList<>();
        for (String s : permissions) {
            if (ContextCompat.checkSelfPermission(activity, s) != PackageManager.PERMISSION_GRANTED) {
                requestList.add(s);
            }
        }
        boolean result = requestList.size() == 0;
        askToUser(activity, result, requestList);
        return result;
    }

    private static void askToUser(Activity activity, boolean result, List<String> permissions) {
        if (!result) {
            List<String> permissionsAllowRequest = new ArrayList<>();
            for (String permission : permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    permissionsAllowRequest.add(permission);
                } else {
                    permissionsAllowRequest.add(permission);
                }
            }
            ActivityCompat.requestPermissions(activity, permissionsAllowRequest.toArray(new String[0]), requestCode);
        }
    }

}