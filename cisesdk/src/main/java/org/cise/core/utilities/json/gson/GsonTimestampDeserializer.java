package org.cise.core.utilities.json.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.sql.Timestamp;

/**
 * Created by user on 03/08/2017.
 */

public class GsonTimestampDeserializer implements JsonDeserializer<Timestamp> {

    @Override
    public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        long time = Long.parseLong(json.getAsString());
        return new Timestamp(time);
    }

}
