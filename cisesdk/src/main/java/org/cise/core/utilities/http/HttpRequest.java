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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * @author zuliadin
 */
public class HttpRequest<T> {

    private final String TAG = "HTTP-REQ";

    private final int MIN_TIMEOUT = 9000;

    private int timeout = 0;

    private String endpoint;

    private String json;

    private HttpResponse.Listener<T> listener;

    public HttpRequest(String url, HttpResponse.Listener<T> listener) {
        initialize(url, MIN_TIMEOUT, json, listener);
    }

    public HttpRequest(String url, int timeout, HttpResponse.Listener<T> listener) {
        initialize(url, timeout, json, listener);
    }

    protected HttpRequest(String url, String json, HttpResponse.Listener<T> listener) {
        initialize(url, MIN_TIMEOUT, json, listener);
    }

    protected HttpRequest(String url, int timeout, String json, HttpResponse.Listener<T> listener) {
        initialize(url, timeout, json, listener);
    }

    private void initialize(String url, int timeout, String json, HttpResponse.Listener<T> listener) {
        this.endpoint = url;
        this.timeout = timeout;
        this.json = json;
        this.listener = listener;
    }

    protected void request() {
        Log.d(TAG, "\nurl : " + endpoint + "\nbody : \n" + json + "\n");
        if (null == endpoint || "".equalsIgnoreCase(endpoint)) return;
        StringBuffer response = new StringBuffer();
        HttpURLConnection conn = null;
        try {
            URL url = new URL(this.endpoint);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setReadTimeout(Math.max(MIN_TIMEOUT, timeout));
            conn.setConnectTimeout(Math.max(MIN_TIMEOUT, timeout));
            if (null == json) {
                conn.setRequestMethod("GET");
                conn.setDoOutput(false); // true is force to post
            } else {
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("connection", "close");
            if (null != json) {
                OutputStream os = conn.getOutputStream();
                os.write(json.getBytes());
                os.flush();
                os.close();
            }
            final int responseCode = conn.getResponseCode();
            if (responseCode >= 200 && responseCode <= 299) {
                InputStream inputStream = conn.getInputStream();
                int size = conn.getContentLength();
                BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
                int readBytes = 0;
                Log.d(TAG, "progress size =" + (size));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                    if (size != -1) {
                        readBytes += inputLine.getBytes("ISO-8859-2").length + 2;
                        Log.d(TAG, "progress =" + (size - readBytes));
                    } else {
                        Log.d(TAG, "content length not available");
                    }
                }
                in.close();
                final Type responseType = getResponseType();
                try {
                    if (null == responseType) {
                        handlerSuccess((T) response.toString());
                    } else {
                        final T jsonResponse = GsonHelper.newInstance().getGson().fromJson(response.toString(), responseType);
                        handlerSuccess(jsonResponse);
                    }
                } catch (JsonSyntaxException e) {
                    HttpError httpError = new HttpError(responseCode);
                    httpError.error(endpoint + "\nCannot convert response \n" + response.toString() + "\n" + json);
                    handlerError(httpError);
                }
            } else {
                handlerError(new HttpError(responseCode));
            }
        } catch (final MalformedURLException ex) {
            HttpError error = new HttpError(ex);
            StringBuilder sb = new StringBuilder();
            sb.append("end poin : ");
            sb.append(endpoint);
            sb.append("\n");
            sb.append(ex.getMessage());
            sb.append("\n");
            sb.append(Math.max(MIN_TIMEOUT, timeout));
            sb.append("\n");
            Log.e(TAG, "HttpResponse : \n" + response.toString());
            for (StackTraceElement e : ex.getStackTrace()) {
                sb.append("\n");
                sb.append(e);
                Log.e(TAG, String.valueOf(e));
            }
            error.error(sb.toString());
            handlerError(error);
        } catch (final SocketTimeoutException ex) {
            HttpError error = new HttpError(ex);
            StringBuilder sb = new StringBuilder();
            sb.append("end poin : ");
            sb.append(endpoint);
            sb.append("\n");
            sb.append(ex.getMessage());
            sb.append("\n");
            sb.append(Math.max(MIN_TIMEOUT, timeout));
            sb.append("\n");
            Log.e(TAG, "HttpResponse : \n" + String.valueOf(response.toString()));
            for (StackTraceElement e : ex.getStackTrace()) {
                sb.append("\n");
                sb.append(e);
                Log.e(TAG, String.valueOf(e));
            }
            error.error(sb.toString());
            handlerError(error);
        } catch (final IOException ex) {
            HttpError httpError = new HttpError(ex);
            StringBuilder sb = new StringBuilder();
            sb.append("end poin : ");
            sb.append(endpoint);
            sb.append("\n");
            sb.append(ex.getMessage());
            sb.append("\n");
            Log.e(TAG, "HttpResponse : \n" + String.valueOf(response.toString()));
            for (StackTraceElement e : ex.getStackTrace()) {
                sb.append("\n");
                sb.append(e);
                Log.e(TAG, String.valueOf(e));
            }
            handlerError(httpError);
        } catch (final Exception ex) {
            HttpError httpError = new HttpError(ex);
            StringBuilder sb = new StringBuilder();
            sb.append("end poin : ");
            sb.append(endpoint);
            sb.append("\n");
            sb.append(ex.getMessage());
            sb.append("\n");
            Log.e(TAG, "HttpResponse : \n" + String.valueOf(response.toString()));
            for (StackTraceElement e : ex.getStackTrace()) {
                sb.append("\n");
                sb.append(e);
                Log.e(TAG, String.valueOf(e));
            }
            handlerError(httpError);
        } finally {
            if (null != conn) {
                conn.disconnect();
            }
        }
    }

    private void handlerSuccess(T response) {
        if (null == listener) return;
        new Handler(Looper.getMainLooper()).post(() -> {
            listener.onSuccess(response);
        });
    }

    private void handlerError(HttpError error) {
        if (null == listener) return;
        new Handler(Looper.getMainLooper()).post(() -> {
            listener.onError(error);
        });
    }

    /**
     * get type from interface
     */
    private Type getResponseType() {
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
