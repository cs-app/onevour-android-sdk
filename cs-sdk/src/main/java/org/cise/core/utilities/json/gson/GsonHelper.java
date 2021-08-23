package org.cise.core.utilities.json.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;

import org.cise.core.utilities.commons.ValueUtils;

import java.util.Date;

/**
 * Created by Zuliadin on 26/01/2017.
 */

public class GsonHelper {

    private static GsonHelper gsonHelper;

    public static Gson gson;

    private GsonHelper() {
        gson = builder().create();
    }

    private GsonBuilder builder() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(String.class, new GsonStringDeserializer());
        gsonBuilder.registerTypeAdapter(String.class, new GsonStringSerializer());
        gsonBuilder.registerTypeAdapter(Date.class, new GsonDateSerializer());
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        return gsonBuilder;
    }

    public void initialize(GsonBuilder gsonBuilder) {
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        gson = gsonBuilder.create();
    }

    public static GsonHelper newInstance() {
        if (ValueUtils.isNull(gson, gsonHelper)) {
            gsonHelper = new GsonHelper();
        }
        return gsonHelper;
    }

    public Gson getGson() {
        return gson;
    }
}
