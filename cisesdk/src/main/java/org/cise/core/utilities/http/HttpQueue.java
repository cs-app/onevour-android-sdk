/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cise.core.utilities.http;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import org.cise.core.utilities.json.gson.GsonHelper;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Zuliadin
 */
public class HttpQueue {

    private static final String TAG = "HttpQueue";
    private final int MAX_POOL = 8;
    private static HttpQueue httpQueue;
    private ExecutorService executor = Executors.newFixedThreadPool(MAX_POOL);

    private HttpQueue() {
        if (null == executor) executor = Executors.newFixedThreadPool(MAX_POOL);
    }

    public static HttpQueue newInstance() {
        if (null == httpQueue) httpQueue = new HttpQueue();
        return httpQueue;
    }

    public void add(HttpRequest httpRequest) {
        if (null == executor) executor = Executors.newFixedThreadPool(MAX_POOL);
        executor.execute(httpRequest::request);
    }

    public <T> void add(final HttpMultipart multipart, final Response.Listener<T> listener) {
        if (null == executor) executor = Executors.newFixedThreadPool(MAX_POOL);
        executor.execute(() -> {
            Handler handler = new Handler(Looper.getMainLooper());
            try {
                multipart.request();
                List<String> response = multipart.finish();
                final StringBuffer responseString = new StringBuffer("");
                for(String s: response){
                    responseString.append(s);
                }
                final Type responseType = getResponseType(listener);
                Log.d(TAG, responseString.toString());
                if (null == responseType) {
                    handler.post(() -> {
                        if (null != listener) {
                            listener.onSuccess((T) response.toString());
                        }

                    });
                } else {
                    String responseResult = response.toString();
                    Log.d(TAG, "Type : "+responseType);
                    if (responseResult.length() > 1 && (responseResult.startsWith("{") && responseResult.endsWith("}") || responseResult.startsWith("[") && responseResult.endsWith("]"))) {
                        String clearTextResponse = responseResult.substring(1, responseResult.toCharArray().length-1);
                        final T jsonResponse = GsonHelper.newInstance().getGson().fromJson(clearTextResponse, responseType);
                        handler.post(() -> {
                            listener.onSuccess(jsonResponse);
                        });
                    } else {
                        handler.post(() -> {
                            Error error = new Error(multipart.getResponseCode());
                            error.error("Cannot convert response");
                            listener.onError(error);
                        });
                    }
                }
            } catch (final IOException e) {
                for(StackTraceElement s: e.getStackTrace()) {
                    Log.e(TAG, String.valueOf(s));
                }
                Log.e(TAG, "upload error " + e.getMessage());
                handler.post(() -> listener.onError(new Error(e)));
            } finally {
                Log.d(TAG, "process upload finish ");
            }
        });
    }

    public void stop() {
        executor.shutdown();
    }

    /**
     * get type from interface
     */
    private <T> Type getResponseType(Response.Listener<T> listener) {
        Type responseType = null;
        if (null != listener) {
            Type[] types = listener.getClass().getGenericInterfaces();
            for (Type type : types) {
                if (type instanceof ParameterizedType) {
                    Type[] gTypes = ((ParameterizedType) type).getActualTypeArguments();
                    for (Type gType : gTypes) {
                        responseType = gType;
                    }
                }
            }
            return responseType;
        } else {
            return null;
        }
    }

}
