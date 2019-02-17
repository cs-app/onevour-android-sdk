package org.cise.core.utilities.json.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by user on 29/01/2017.
 */

public class GsonStringSerializer implements JsonSerializer<String> {

    public JsonElement serialize(String src, Type typeOfSrc, JsonSerializationContext context) {
        if (null != src) {
            return new JsonPrimitive(src.trim());
        } else {
            return new JsonPrimitive("");
        }
    }

}


