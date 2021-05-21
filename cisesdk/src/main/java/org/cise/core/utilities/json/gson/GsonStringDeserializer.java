package org.cise.core.utilities.json.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by Zuliadin on 26/01/2017.
 */

public class GsonStringDeserializer implements JsonDeserializer<String> {

    @Override
    public String deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (element.isJsonObject() || element.isJsonArray()) {
            return element.toString();
        } else {
            return getNullAsEmptyString(element);
        }
    }

    private String getNullAsEmptyString(JsonElement jsonElement) {
        return jsonElement.isJsonNull() ? null : jsonElement.getAsString().trim();
    }

}