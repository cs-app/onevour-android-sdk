package org.cise.core.utilities.json.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Zuliadin on 26/01/2017.
 */

public class GsonDateDeserializer implements JsonDeserializer<Date> {

    // jangan di rubah urutanya
    private final SimpleDateFormat[] formats = {
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault()),
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault()),
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()),
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()),
            new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()),
            new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()),
            new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()),
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.getDefault())
    };

    @Override
    public Date deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (null == element) return null;
        String dateString = element.getAsString().trim();
        if (dateString.isEmpty()) return null;

        try {
            return new Date(Long.parseLong(dateString));
        } catch (NumberFormatException ignore) {
        }
        for (SimpleDateFormat format : formats) {
            try {
                Date date = format.parse(dateString);
                return date;
            } catch (NumberFormatException ignore) {
            } catch (ParseException ignore) {
            }
        }
        return null;
    }


}
