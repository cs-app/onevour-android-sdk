package org.cise.core.utilities.json.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;

import java.util.Date;

/**
 * Created by Zuliadin on 26/01/2017.
 */

public class GsonHelper {

    private static GsonHelper gsonHelper;

    public static Gson gson;

    private GsonHelper() {
        gson = builder(null).create();
        //
//        gsonBuilder.registerTypeAdapter(Date.class, new GsonDateDeserializer());
//        gsonBuilder.registerTypeAdapter(Date.class, new GsonDateSerializer());
//        gsonBuilder.registerTypeAdapter(Date.class, new DateTypeAdapter());
    }

    private GsonBuilder builder(String patternDate) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(String.class, new GsonStringDeserializer());
        gsonBuilder.registerTypeAdapter(String.class, new GsonStringSerializer());
        if (patternDate == null) {
            gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        } else {
            gsonBuilder.setDateFormat(patternDate);
        }
        gsonBuilder.excludeFieldsWithoutExposeAnnotation();
        return gsonBuilder;
    }

    public void initialize(String patternDate) {
        gson = builder(patternDate).create();
    }

    public static GsonHelper newInstance() {
        if (null == gsonHelper || null == gson) {
            gsonHelper = new GsonHelper();
        }
        return gsonHelper;
    }

    public Gson getGson() {
        return gson;
    }
}
