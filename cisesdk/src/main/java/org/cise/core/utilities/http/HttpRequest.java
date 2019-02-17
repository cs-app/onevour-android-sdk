/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cise.core.utilities.http;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.cise.core.utilities.json.gson.GsonHelper;

import java.io.BufferedReader;
import java.io.IOException;
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
public class HttpRequest {

    private static final String TAG = "CiseHTTP";
    private static final int MIN_TIMEOUT = 6000;
    private static final int READ_TIMEOUT = 30000;
    private int timeout = 0;
    private String endpoin;
    private String json;
    private Response.Listener listener;

    public HttpRequest(String url, Response.Listener<?> listener) {
        initialize(url, MIN_TIMEOUT, json, listener);
    }

    public HttpRequest(String url, int timeout, Response.Listener<?> listener) {
        initialize(url, timeout, json, listener);
    }

    protected HttpRequest(String url, String json, Response.Listener<?> listener) {
        initialize(url, MIN_TIMEOUT, json, listener);
    }

    protected HttpRequest(String url, int timeout, String json, Response.Listener<?> listener) {
        initialize(url, timeout, json, listener);
    }

    private void initialize(String url, int timeout, String json, Response.Listener<?> listener) {
        this.endpoin = url;
        this.timeout = timeout;
        this.json = json;
        this.listener = listener;
    }

    @SuppressWarnings("unchecked")
    protected void request() {
        Log.d(TAG, "\nurl : " + String.valueOf(endpoin)+"\nbody : " + String.valueOf(json));
        Log.d(TAG, "Request : \n" + String.valueOf(json));
        if (null == endpoin || "".equalsIgnoreCase(endpoin)) return;
        Handler handler = new Handler(Looper.getMainLooper());
        final StringBuffer response = new StringBuffer();
        HttpURLConnection conn = null;
        try {
            URL url = new URL(this.endpoin);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(timeout < MIN_TIMEOUT ? MIN_TIMEOUT : timeout);
            conn.setRequestMethod(null != json ? "POST" : "GET");
            conn.setDoOutput(true);
            conn.setRequestProperty("connection", "close");
            if (null != json) {
                OutputStream os = conn.getOutputStream();
                os.write(json.getBytes());
                os.flush();
                os.close();
            }
            final int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                Log.d(TAG, "Response : \n" + String.valueOf(response.toString()));
                final Type responseType = getResponseType();
                Log.d(TAG, "Response Type: \n" + String.valueOf(responseType));
                if (null == responseType) {
                    handler.post(() -> {
                        if (null != listener)
                            listener.onSuccess(response.toString());
                    });
                } else {
                    String responseResult = response.toString();
                    if (responseResult.length() > 1 && (responseResult.startsWith("{") && responseResult.endsWith("}") || responseResult.startsWith("[") && responseResult.endsWith("]"))) {
                        final Object jsonResponse = GsonHelper.newInstance().getGson().fromJson(response.toString(), responseType);
                        handler.post(() -> {
                            if (null != listener)
                                listener.onSuccess(jsonResponse);
                        });
                    } else {
                        handler.post(() -> {
                            if (null != listener) {
                                Error error = new Error(responseCode);
                                error.error("Cannot convert response");
                                listener.onError(error);
                            }
                        });
                    }
                }
            } else {
                handler.post(() -> {
                    if (null != listener)
                        listener.onError(new Error(responseCode));
                });
            }
        } catch (final MalformedURLException ex) {
            handler.post(() -> {
                if (null != listener) listener.onError(new Error(ex));
            });
            StringBuilder sb = new StringBuilder();
            sb.append("end poin : ");
            sb.append(endpoin);
            sb.append("\n");
            sb.append(ex.getMessage());
            sb.append("\n");
            Log.e(TAG, "Response : \n" + String.valueOf(response.toString()));
            for (StackTraceElement e : ex.getStackTrace()) {
                sb.append("\n");
                sb.append(e);
                Log.e(TAG, String.valueOf(e));
            }
        } catch (final SocketTimeoutException ex) {
            handler.post(() -> {
                if (null != listener)
                    listener.onError(new Error(ex, "Socket timeout after " + READ_TIMEOUT + " ms"));
            });
            StringBuilder sb = new StringBuilder();
            sb.append("end poin : ");
            sb.append(endpoin);
            sb.append("\n");
            sb.append(ex.getMessage());
            sb.append("\n");
            Log.e(TAG, "Response : \n" + String.valueOf(response.toString()));
            for (StackTraceElement e : ex.getStackTrace()) {
                sb.append("\n");
                sb.append(e);
                Log.e(TAG, String.valueOf(e));
            }
        } catch (final IOException ex) {
            handler.post(() -> {
                if (null != listener) listener.onError(new Error(ex));
            });
            StringBuilder sb = new StringBuilder();
            sb.append("end poin : ");
            sb.append(endpoin);
            sb.append("\n");
            sb.append(ex.getMessage());
            sb.append("\n");
            Log.e(TAG, "Response : \n" + String.valueOf(response.toString()));
            for (StackTraceElement e : ex.getStackTrace()) {
                sb.append("\n");
                sb.append(e);
                Log.e(TAG, String.valueOf(e));
            }
        } catch (final Exception ex) {
            handler.post(() -> {
                if (null != listener) listener.onError(new Error(ex));
            });
            StringBuilder sb = new StringBuilder();
            sb.append("end poin : ");
            sb.append(endpoin);
            sb.append("\n");
            sb.append(ex.getMessage());
            sb.append("\n");
            Log.e(TAG, "Response : \n" + String.valueOf(response.toString()));
            for (StackTraceElement e : ex.getStackTrace()) {
                sb.append("\n");
                sb.append(e);
                Log.e(TAG, String.valueOf(e));
            }
        } finally {
            if (null != conn) {
                conn.disconnect();
            }
        }
    }

    /**
     * get type from interface
     */
    private Type getResponseType() {
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
