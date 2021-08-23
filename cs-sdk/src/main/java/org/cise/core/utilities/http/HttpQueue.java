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
public class HttpQueue {

    private static final String TAG = HttpQueue.class.getSimpleName();
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

    @SuppressWarnings({"rawtypes"})
    public void add(HttpRequest httpRequest) {
        if (null == executor) executor = Executors.newFixedThreadPool(MAX_POOL);
        executor.execute(httpRequest::request);
    }

    @SuppressWarnings({"unchecked"})
    public <T> void add(final HttpMultipart multipart, final HttpResponse.Listener<T> listener) {
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
                        HttpError httpError = new HttpError(multipart.getResponseCode());
                        httpError.error("Cannot convert response \n:".concat(responseResult));
                        listener.onError(httpError);
                    }
                }
            } catch (final IOException e) {
                for (StackTraceElement s : e.getStackTrace()) {
                    Log.e(TAG, String.valueOf(s));
                }
//                Log.e(TAG, "upload http error " + e.getMessage());
                HttpError httpError = new HttpError(e);
                handler.post(() -> listener.onError(httpError));
            } catch (JsonSyntaxException e) {
                HttpError httpError = new HttpError(multipart.getResponseCode());
//                httpError.error("Cannot convert response \n:" + responseString.toString());
                listener.onError(httpError);
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
    private <T> Type getResponseType(HttpResponse.Listener<T> listener) {
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
