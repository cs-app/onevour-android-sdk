package com.onevour.core.utilities.json.gson;

import android.icu.text.SimpleDateFormat;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.onevour.core.utilities.commons.ValueOf;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Zuliadin on 29/01/2017.
 */

public class GsonDateSerializer implements JsonDeserializer<Date> {

    final private String[] formatStr = {
            "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd",
            "HH:mm:ss"
    };

//    final private SimpleDateFormat[] formats = {
//            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault()),
//            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()),
//            new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()),
//            new SimpleDateFormat("HH:mm:ss", Locale.getDefault())
//    };

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        String j = json.getAsJsonPrimitive().getAsString();
        return parseDate(j);
//        try {
//        } catch (ParseException e) {
//            throw new JsonParseException(e.getMessage(), e);
//        }
    }

    private Date parseDate(String dateString) {
        if (ValueOf.isEmpty(dateString)) {
            return null;
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            for (String format : formatStr) {
                try {
                    return new SimpleDateFormat(format, Locale.getDefault()).parse(dateString);
                } catch (ParseException e) {
                    // ignore
                }
            }
        } else {
            for (String format : formatStr) {
                try {
                    return new java.text.SimpleDateFormat(format, Locale.getDefault()).parse(dateString);
                } catch (ParseException e) {
                    // ignore
                }
            }
        }
        return null;
    }

}


