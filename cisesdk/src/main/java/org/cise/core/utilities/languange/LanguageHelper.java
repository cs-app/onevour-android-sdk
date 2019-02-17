package org.cise.core.utilities.languange;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;

import java.util.Locale;

/**
 * Created by user on 24/06/2017.
 */

public class LanguageHelper {

    public static void setLanguage(Context context, String s) {
        if (null == s) s = "en";
        Locale locale = new Locale(s);
        Locale.setDefault(locale);
        Configuration config = new Configuration(context.getResources().getConfiguration());
        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale);
        } else {
            config.locale = locale;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            context.createConfigurationContext(config);
        } else {
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        }
        */
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }
}
