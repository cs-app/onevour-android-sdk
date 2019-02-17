package org.cise.core.utilities.json.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

/**
 * Created by user on 26/01/2017.
 */

public class GsonHelper {
    private static GsonHelper gsonHelper;
    private static Gson gson;

    private GsonHelper(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(String.class, new GsonStringDeserializer());
        gsonBuilder.registerTypeAdapter(String.class, new GsonStringSerializer());
        gsonBuilder.registerTypeAdapter(Date.class, new GsonDateDeserializer());
        gsonBuilder.registerTypeAdapter(Date.class, new GsonDateSerializer());
        gson = gsonBuilder.create();
    }
    public static GsonHelper newInstance(){
        if(null==gsonHelper||null==gson) {
            gsonHelper = new GsonHelper();
        }
        return gsonHelper;
    }

    public Gson getGson() {
        return gson;
    }
}
