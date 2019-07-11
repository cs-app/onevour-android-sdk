/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cise.core.utilities.http;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

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

    private static final String TAG = "CiseHTTP";
    private static final int MIN_TIMEOUT = 6000;
    private static final int READ_TIMEOUT = 30000;
    private int timeout = 0;
    private String endpoin;
    private String json;
    private Context context;

    private Response.Listener<T> listener;

    public HttpRequest(Context context, String url, Response.Listener<T> listener) {
        initialize(context, url, MIN_TIMEOUT, json, listener);
    }

    public HttpRequest(Context context, String url, int timeout, Response.Listener<T> listener) {
        initialize(context, url, timeout, json, listener);
    }

    protected HttpRequest(Context context, String url, String json, Response.Listener<T> listener) {
        initialize(context, url, MIN_TIMEOUT, json, listener);
    }

    protected HttpRequest(Context context,String url, int timeout, String json, Response.Listener<T> listener) {
        initialize(context,url, timeout, json, listener);
    }

    private void initialize(Context context, String url, int timeout, String json, Response.Listener<T> listener) {
        this.context = context;
        this.endpoin = url;
        this.timeout = timeout;
        this.json = json;
        this.listener = listener;
    }

    protected void request() {
        Log.d(TAG, "\nurl : " + endpoin + "\nbody : \n" + json + "\n");
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
                Log.d(TAG, "Response Type: \n" + String.valueOf(responseType));
                Log.d(TAG, "Response : \n" + String.valueOf(response.toString()));
                if (null == responseType) {
                    handler.post(() -> {
                        if (null != listener && null != context)
                            listener.onSuccess((T) response.toString());
                    });
                } else {
                    String responseResult = response.toString();
                    if (responseResult.length() > 1 && (responseResult.startsWith("{") && responseResult.endsWith("}") || responseResult.startsWith("[") && responseResult.endsWith("]"))) {
                        final T jsonResponse = GsonHelper.newInstance().getGson().fromJson(response.toString(), responseType);
                        handler.post(() -> {
                            if (null != listener && null != context)
                                listener.onSuccess(jsonResponse);
                        });
                    } else {
                        handler.post(() -> {
                            if (null != listener && null != context) {
                                Error error = new Error(responseCode);
                                error.error("Cannot convert response");
                                listener.onError(error);
                            }
                        });
                    }
                }
            } else {
                handler.post(() -> {
                    if (null != listener && null != context)
                        listener.onError(new Error(responseCode));
                });
            }
        } catch (final MalformedURLException ex) {
            handler.post(() -> {
                if (null != listener && null != context) listener.onError(new Error(ex));
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
                if (null != listener && null != context)
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
                if (null != listener && null != context) listener.onError(new Error(ex));
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
                if (null != listener && null != context) listener.onError(new Error(ex));
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
        if (null != listener && null != context) {
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
