package org.cise.core.utilities.http;

import org.cise.core.utilities.json.gson.GsonHelper;

import java.util.Map;

/**
 * Created by Zuliadin on 09/01/2017.
 */

@SuppressWarnings({"unchecked", "rawtypes"})
public class ApiRequest {

    private static final String TAG = ApiRequest.class.getSimpleName();

    private static ApiQueue queue() {
        return ApiQueue.newInstance();
    }

    public static <T> void get(String url, HttpListener<T> listener) {
        queue().add(new HttpRequest(url, listener));
    }

    public static <T> void get(String url, Map<String, String> header, HttpListener<T> listener) {
        queue().add(new HttpRequest(url, header, listener));
    }

    public static <T> void post(String url, T json) {
        post(url, json, null);
    }

    public static <T, E> void post(String url, T json, HttpListener<E> listener) {
        if (json instanceof String) {
            queue().add(new HttpRequest(url, (String) json, listener));
        } else {
            String body = GsonHelper.newInstance().getGson().toJson(json);
            queue().add(new HttpRequest(url, body, listener));
        }
    }

    public static <T, E> void post(String url, Map<String, String> header, T json, HttpListener<E> listener) {
        if (json instanceof String) {
            queue().add(new HttpRequest(url, header, (String) json, listener));
        } else {
            String body = GsonHelper.newInstance().getGson().toJson(json);
            queue().add(new HttpRequest(url, header, body, listener));
        }
    }

    public static <T, E> void post(String url, int timeout, T json, HttpListener<E> listener) {
        if (json instanceof String) {
            queue().add(new HttpRequest(url, timeout, (String) json, listener));
        } else {
            queue().add(new HttpRequest(url, timeout, GsonHelper.newInstance().getGson().toJson(json), listener));
        }
    }

    public static <T> void post(HttpMultipart request, HttpListener<T> listener) {
        queue().add(request, listener);
    }

}
