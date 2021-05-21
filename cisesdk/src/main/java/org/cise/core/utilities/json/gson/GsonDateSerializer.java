package org.cise.core.utilities.json.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.cise.core.utilities.commons.ValueUtils;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Zuliadin on 29/01/2017.
 */

public class GsonDateSerializer implements JsonDeserializer<Date> {

    final private SimpleDateFormat[] formats = {
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault()),
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()),
            new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()),
            new SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    };

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            String j = json.getAsJsonPrimitive().getAsString();
            return parseDate(j);
        } catch (ParseException e) {
            throw new JsonParseException(e.getMessage(), e);
        }
    }

    private Date parseDate(String dateString) throws ParseException {
        if (ValueUtils.isEmpty(dateString)) {
            return null;
        }
        for (SimpleDateFormat dateFormat : formats) {
            try {
                return dateFormat.parse(dateString);
            } catch (ParseException e) {
                // ignore
            }
        }
        return null;
    }

}


