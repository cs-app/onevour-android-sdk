package org.cise.core.utilities.gps;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.appcompat.app.AlertDialog;
import android.widget.Toast;

/**
 * Created by zuliadin on 12/04/2017.
 */

public class GPSHelper {

    public static boolean autoActive(final Context context) {
        String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        StringBuilder stringBuilder = new StringBuilder("start check gps active.");
        if (!provider.contains("gps") && Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            Toast.makeText(context, "!provider.contains(\"gps\") && Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT", Toast.LENGTH_SHORT).show();
            stringBuilder.append(" gps not active and version below kitkat, auto open gps!");
            Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
            intent.putExtra("enabled", true);
            context.sendBroadcast(intent);
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            context.sendBroadcast(poke);
        } else if (!provider.contains("gps") && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            stringBuilder.append(" gps not active and version > kitkat, auto open dialog gps config!");
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
            alertBuilder.setTitle("Location not available, Open GPS?")
                    .setMessage("Activate GPS to use use location services?")
                    .setPositiveButton("Open Settings", (dialog, which) -> {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(intent);
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel()).show();
        } else if (!provider.contains("gps")) {
            stringBuilder.append(" gps not active!");
            Toast.makeText(context, "GPS tidak aktif", Toast.LENGTH_LONG).show();
        }
        return provider.contains("gps");
    }

    public void turnGPSOff(Context context) {
        String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (provider.contains("gps")) {
            //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            context.sendBroadcast(poke);
        }
    }
}
