package org.cise.core.utilities.connection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zuliadin on 16/01/2017.
 */

public class ConnectionUtils {

    private static final String TAG = "ConnUtl";

    public static boolean isNetworkAvailable(Context context) {
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean result = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (!result) {
            Toast.makeText(context, "Network not available", Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    public static boolean isWifi(Context context) {
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean result = (null != activeNetwork && (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI));
        if (result && activeNetwork.isConnectedOrConnecting()) {
            //Toast.makeText(context, "Network not available on wifi connection", Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    public static boolean isWifiMessage(Context context) {
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean result = (null != activeNetwork && (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI));
        if (result && activeNetwork.isConnectedOrConnecting()) {
            Toast.makeText(context, "Network not available on wifi connection", Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    public static boolean isWifiHasConnection(Context context) {
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean result = (null != activeNetwork && (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI && activeNetwork.isConnectedOrConnecting()));
        if (!result) {
            Toast.makeText(context, "Network not available on wifi connection", Toast.LENGTH_SHORT).show();
        }
        return result;
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static PingStatus pingUrl(int timeout, String ip) throws IOException {
        return ping(timeout, getIp(ip));
    }

    public static PingStatus pingUrl(String ip) throws IOException {
        return ping(7, getIp(ip));
    }

    private static PingStatus ping(int timeout, String ip) throws IOException {
        StringBuilder output = new StringBuilder();
        Process process = Runtime.getRuntime().exec("/system/bin/ping -w " + timeout + " " + ip);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        int i;
        char[] buffer = new char[4096];
        while ((i = reader.read(buffer)) > 0) {
            output.append(buffer, 0, i);
        }
        reader.close();
        return new PingStatus(output.toString());
    }

    private static String getIp(String url) {
        if (url != null && !url.isEmpty()) {
            String[] segment = url.split("//");
            if (segment.length > 0) {
                String[] ipPorts = segment[1].split(":");
                Log.d(TAG, "\n\nIP : " + ipPorts[0] + "\nfrom url : " + url);
                return ipPorts[0];
            }
            return "";
        } else {
            return "";
        }
    }

    public static class PingStatus {

        private boolean isReachable;
        private String caption;
        private double minTime = -1, avgTime = -1, maxTime = -1;
        private String transmitInfo;
        private String rttInfo;
        private String fullMessage;
        private List<Double> pingSeqTime = new ArrayList<>();

        PingStatus(String message) {
            isReachable = message != null && !message.isEmpty();
            if (isReachable) {
                fullMessage = message;
                String[] lines = fullMessage.split(System.getProperty("line.separator"));
                for (String line : lines) {
                    reader(line);
                }
            }
            Log.d(TAG, "ping : " + minTime + "/" + avgTime + "/" + maxTime);
        }

        private void reader(String s) {
            if (s.startsWith("PING")) {
                caption = s;
            } else if (s.startsWith("64 bytes from")) {
                String[] infoLine = s.split(":");
                String[] infoLineColumns = infoLine[1].trim().split(" ");
                String[] infoLineICMTime = infoLineColumns[2].split("=");
                pingSeqTime.add(Double.valueOf(infoLineICMTime[1].replaceAll(" ms", "")));
                Log.d(TAG, s);
            } else if (s.startsWith(pingSeqTime.size() + " packets transmitted")) {
                transmitInfo = s;
            } else if (s.startsWith("rtt")) {
                rttInfo = s.split("=")[1];
                String[] rttValue = rttInfo.trim().split(" ")[0].split("/");
                Log.d(TAG, String.valueOf(rttValue));
                minTime = Double.valueOf(rttValue[1]);
                avgTime = Double.valueOf(rttValue[2]);
                maxTime = Double.valueOf(rttValue[3]);//.replaceAll("ms", ""));
            }
        }

        public String getCaption() {
            return caption;
        }

        public double getMinTime() {
            return minTime;
        }

        public double getAvgTime() {
            if (isEmulator()) {
                return 20;
            } else return avgTime;
        }

        public double getMaxTime() {
            return maxTime;
        }

        public String getTransmitInfo() {
            return transmitInfo;
        }

        public String getRttInfo() {
            return rttInfo;
        }

        public String getFullMessage() {
            return fullMessage;
        }

        public boolean isReachable() {
            if (isEmulator()) {
                return true;
            } else return isReachable && pingSeqTime.size() > 0;
        }

        public int getPingSeqTime() {
            return pingSeqTime.size();
        }

        public boolean isEmulator() {
            return Build.FINGERPRINT.startsWith("generic")
                    || Build.FINGERPRINT.startsWith("unknown")
                    || Build.MODEL.contains("google_sdk")
                    || Build.MODEL.contains("Emulator")
                    || Build.MODEL.contains("Android SDK built for x86")
                    || Build.MANUFACTURER.contains("Genymotion")
                    || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                    || "google_sdk".equals(Build.PRODUCT);
        }

    }
}
