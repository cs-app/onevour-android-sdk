/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cise.core.utilities.http;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
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

    public void add(final HttpMultipart multipart, final Response.Listener listener) {
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
                handler.post(() -> listener.onSuccess(responseString.toString()));
                Log.d(TAG, "upload success");
            } catch (final IOException e) {
                Log.e(TAG, "upload error " + String.valueOf(e.getMessage()));
                handler.post(() -> listener.onError(new Error(e)));
            } finally {
                Log.d(TAG, "process upload finish ");
            }
        });
    }

    public void stop() {
        executor.shutdown();
    }

}
