package org.cise.core.utilities.format;



import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DTFormat {

    public final static Locale ALC_LOCAL = new Locale("in", "ID");
    public final static SimpleDateFormat SDF_GENERAL = new SimpleDateFormat("yyyy-MM-dd", ALC_LOCAL);
    public final static SimpleDateFormat SDF_TIME_GENERAL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", ALC_LOCAL);
    public final static SimpleDateFormat SDF_TIME_HOUR_GENERAL = new SimpleDateFormat("HH:mm", ALC_LOCAL);
    public final static SimpleDateFormat SDF_DATETIME_GENERAL = new SimpleDateFormat("yyyyMMddHHmmss", ALC_LOCAL);
    public final static SimpleDateFormat SDF_DATETIME_GENERAL_ID = new SimpleDateFormat("yyMMddHHmmssS", ALC_LOCAL);


    public static String now() {
        return SDF_GENERAL.format(new Date());
    }

    public static String nowFull() {
        return SDF_DATETIME_GENERAL.format(new Date());
    }

    public static String nowUnique() {
        return SDF_DATETIME_GENERAL_ID.format(new Date());
    }

    public static String nowReadable() {
        return SDF_TIME_GENERAL.format(new Date());
    }
}
