package org.cise.core.utilities.http;

import android.util.Log;

import org.cise.core.utilities.json.gson.GsonHelper;

/**
 * Created by user on 09/01/2017.
 */

public class APIHitter {

    private static final String TAG = "APIHitter";

    private static HttpQueue queue(){
        return HttpQueue.newInstance();
    }

    public static <T> void get(String url, Response.Listener<T> listener) {
        queue().add(new HttpRequest(url, listener));
    }

    public static <T> void post(String url, T json) {
        post(url, json, null);
    }

    public static <T,E> void post(String url, T json, Response.Listener<E> listener) {
        if (json instanceof String) {
            queue().add(new HttpRequest(url, (String) json, listener));
        } else {
            String body = GsonHelper.newInstance().getGson().toJson(json);
            queue().add(new HttpRequest(url, body, listener));
        }
    }

    public static <T,E> void post(String url, int timeout, T json, Response.Listener<E> listener) {
        if (json instanceof String) {
            queue().add(new HttpRequest(url, timeout, (String) json, listener));
        } else {
            queue().add(new HttpRequest(url, timeout, GsonHelper.newInstance().getGson().toJson(json), listener));
        }
    }

    public static void post(HttpMultipart request, Response.Listener listener) {
        queue().add(request, listener);
    }

}
