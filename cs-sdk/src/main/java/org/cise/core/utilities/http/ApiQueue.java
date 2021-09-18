/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cise.core.utilities.http;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.JsonSyntaxException;

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
public class ApiQueue {

    private static final String TAG = ApiQueue.class.getSimpleName();

    private final int MAX_POOL = 16;

    private static ApiQueue apiQueue;

    private ExecutorService executor = Executors.newFixedThreadPool(MAX_POOL);

    private ApiQueue() {
        if (null == executor) executor = Executors.newFixedThreadPool(MAX_POOL);
    }

    public static ApiQueue newInstance() {
        if (null == apiQueue) apiQueue = new ApiQueue();
        return apiQueue;
    }

    @SuppressWarnings({"rawtypes"})
    public void add(HttpRequest httpRequest) {
        if (null == executor) executor = Executors.newFixedThreadPool(MAX_POOL);
        executor.execute(httpRequest::request);
    }

    @SuppressWarnings({"unchecked"})
    public <T> void add(final HttpMultipart multipart, final HttpListener<T> listener) {
        if (null == executor) executor = Executors.newFixedThreadPool(MAX_POOL);
        executor.execute(() -> {
            if (null == listener) return;
            StringBuffer responseString = new StringBuffer("");
            Handler handler = new Handler(Looper.getMainLooper());
            try {
                multipart.request();
                List<String> response = multipart.finish();
                for (String s : response) {
                    responseString.append(s);
                }
                final Type responseType = getResponseType(listener);
                Log.d(TAG, responseString.toString());
                if (null == responseType) {
                    handler.post(() -> {
                        listener.onSuccess((T) responseString.toString());
                    });
                } else {
                    String responseResult = response.toString();
                    try {
                        final T jsonResponse = GsonHelper.newInstance().getGson().fromJson(responseResult, responseType);
                        handler.post(() -> {
                            listener.onSuccess(jsonResponse);
                        });
                    } catch (JsonSyntaxException e) {
                        Error error = new Error(multipart.getResponseCode());
                        error.error("Cannot convert response \n:".concat(responseResult));
                        listener.onError(error);
                    }
                }
            } catch (final IOException e) {
                for (StackTraceElement s : e.getStackTrace()) {
                    Log.e(TAG, String.valueOf(s));
                }
                Error error = new Error(e);
                handler.post(() -> listener.onError(error));
            } catch (JsonSyntaxException e) {
                Error error = new Error(multipart.getResponseCode());
                listener.onError(error);
            } finally {
                Log.d(TAG, "process upload finish");
            }
        });
    }

    public void stop() {
        executor.shutdown();
    }

    /**
     * get type from interface
     */
    private <T> Type getResponseType(HttpListener<T> listener) {
        if (null == listener) return null;
        Type[] types = listener.getClass().getGenericInterfaces();
        for (Type type : types) {
            if (type instanceof ParameterizedType) {
                Type[] gTypes = ((ParameterizedType) type).getActualTypeArguments();
                for (Type gType : gTypes) {
                    return gType;
                }
            }
        }
        return null;
    }

}
