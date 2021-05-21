package org.cise.core.utilities.format;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DTFormat {

    public final static Locale local = new Locale("in", "ID");
    public final static SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd", local);
    public final static SimpleDateFormat yyyyMMddHHmmss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", local);
    public final static SimpleDateFormat HHmm = new SimpleDateFormat("HH:mm", local);
    public final static SimpleDateFormat yyyyMMddHHmmssRandom = new SimpleDateFormat("yyyyMMddHHmmss", local);
    public final static SimpleDateFormat yyyyMMddHHmmssRandomId = new SimpleDateFormat("yyMMddHHmmssS", local);

    public static String now() {
        return yyyyMMdd.format(new Date());
    }

    public static String nowFull() {
        return yyyyMMddHHmmssRandom.format(new Date());
    }

    public static String format(Date date) {
        return yyyyMMdd.format(date);
    }

    public static String formatTime(Date date) {
        return HHmm.format(date);
    }

    public static String formatRandom(Date date) {
        return yyyyMMddHHmmssRandom.format(date);
    }

    public static String formatRandomId(Date date) {
        return yyyyMMddHHmmssRandomId.format(date);
    }

    public static String nowRandom() {
        return yyyyMMddHHmmss.format(new Date());
    }

    public static String nowRandomId() {
        return yyyyMMddHHmmssRandomId.format(new Date());
    }

}
