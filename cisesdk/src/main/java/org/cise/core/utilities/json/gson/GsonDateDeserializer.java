package org.cise.core.utilities.json.gson;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by user on 26/01/2017.
 */

public class GsonDateDeserializer implements JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        Log.d("Deser", String.valueOf(type));
        if (null == element) {
            return null;
        } else {
//                    formatter2.setTimeZone(TimeZone.getTimeZone("GMT"));
            String date = element.getAsString().trim();
            try {
                if (date.isEmpty()) {
                    return null;
                } else if (date.length() == 10) {
                    SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    formatter3.setTimeZone(TimeZone.getDefault());
                    return formatter3.parse(date);
                } else if (date.length() == 16) {
                    SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                    formatter2.setTimeZone(TimeZone.getDefault());
                    return formatter2.parse(date);
                } else if (date.length() == 19) {
                    SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    formatter1.setTimeZone(TimeZone.getDefault());
                    return formatter1.parse(date);
                } else {
                    return null;
                }
            } catch (ParseException e) {
                Log.i("GSON_FORMAT_DATE", "Failed to parse Date due to:", e);
                return null;
            }
        }
    }


}
