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
        queue().add(new HttpRequest(url, "GET", null, null, listener));
    }

    public static <T> void get(String url, Map<String, String> header, HttpListener<T> listener) {
        queue().add(new HttpRequest(url, "GET", header, null, listener));
    }

    public static <T, E> void post(String url, T json, HttpListener<E> listener) {
        if (json instanceof String) {
            queue().add(new HttpRequest(url, "POST", null, (String) json, listener));
        } else {
            String body = GsonHelper.newInstance().getGson().toJson(json);
            queue().add(new HttpRequest(url, "POST", null, body, listener));
        }
    }

    public static <T, E> void post(String url, Map<String, String> header, T json, HttpListener<E> listener) {
        if (json instanceof String) {
            queue().add(new HttpRequest(url, "POST", header, (String) json, listener));
        } else {
            String body = GsonHelper.newInstance().getGson().toJson(json);
            queue().add(new HttpRequest(url, "POST", header, body, listener));
        }
    }

    public static <T, E> void post(String url, int timeout, T json, HttpListener<E> listener) {
        if (json instanceof String) {
            queue().add(new HttpRequest(url, "POST", timeout, (String) json, listener));
        } else {
            queue().add(new HttpRequest(url, "POST", timeout, GsonHelper.newInstance().getGson().toJson(json), listener));
        }
    }

    public static <T, E> void put(String url, Map<String, String> header, T json, HttpListener<E> listener) {
        if (json instanceof String) {
            queue().add(new HttpRequest(url, "PUT", header, (String) json, listener));
        } else {
            String body = GsonHelper.newInstance().getGson().toJson(json);
            queue().add(new HttpRequest(url, "PUT", header, body, listener));
        }
    }

    public static <T, E> void put(String url, int timeout, T json, HttpListener<E> listener) {
        if (json instanceof String) {
            queue().add(new HttpRequest(url, "PUT", timeout, (String) json, listener));
        } else {
            queue().add(new HttpRequest(url, "PUT", timeout, GsonHelper.newInstance().getGson().toJson(json), listener));
        }
    }

    public static <T, E> void delete(String url, T json, HttpListener<E> listener) {
        if (json instanceof String) {
            queue().add(new HttpRequest(url, "DELETE", null, (String) json, listener));
        } else {
            String body = GsonHelper.newInstance().getGson().toJson(json);
            queue().add(new HttpRequest(url, "DELETE", null, body, listener));
        }
    }

    public static <T, E> void delete(String url, Map<String, String> header, T json, HttpListener<E> listener) {
        if (json instanceof String) {
            queue().add(new HttpRequest(url, "DELETE", header, (String) json, listener));
        } else {
            String body = GsonHelper.newInstance().getGson().toJson(json);
            queue().add(new HttpRequest(url, "DELETE", header, body, listener));
        }
    }

    public static <T, E> void delete(String url, int timeout, T json, HttpListener<E> listener) {
        if (json instanceof String) {
            queue().add(new HttpRequest(url, "DELETE", timeout, (String) json, listener));
        } else {
            queue().add(new HttpRequest(url, "DELETE", timeout, GsonHelper.newInstance().getGson().toJson(json), listener));
        }
    }

    public static <T> void post(HttpMultipart request, HttpListener<T> listener) {
        queue().add(request, listener);
    }

}
