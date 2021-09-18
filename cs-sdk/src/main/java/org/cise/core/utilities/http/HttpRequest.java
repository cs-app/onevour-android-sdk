/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cise.core.utilities.http;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.JsonParseException;

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
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * @author zuliadin
 */
public class HttpRequest<T> {

    private final String TAG = HttpRequest.class.getSimpleName();

    private final int MIN_TIMEOUT = 12000;

    private int timeout = 0;

    private String endpoint;

    private Map<String, String> header;

    private String body;

    private HttpListener<T> listener;

    // GET
    public HttpRequest(String url, HttpListener<T> listener) {
        initialize(url, MIN_TIMEOUT, null, body, listener);
    }

    // GET
    public HttpRequest(String url, int timeout, HttpListener<T> listener) {
        initialize(url, timeout, null, body, listener);
    }

    // GET
    public HttpRequest(String url, Map<String, String> header, HttpListener<T> listener) {
        initialize(url, MIN_TIMEOUT, header, body, listener);
    }

    // GET
    public HttpRequest(String url, int timeout, Map<String, String> header, HttpListener<T> listener) {
        initialize(url, timeout, header, body, listener);
    }

    // POST
    protected HttpRequest(String url, String body, HttpListener<T> listener) {
        initialize(url, MIN_TIMEOUT, null, body, listener);
    }

    // POST
    protected HttpRequest(String url, int timeout, String body, HttpListener<T> listener) {
        initialize(url, timeout, null, body, listener);
    }

    // POST
    protected HttpRequest(String url, Map<String, String> header, String body, HttpListener<T> listener) {
        initialize(url, MIN_TIMEOUT, header, body, listener);
    }

    // POST
    protected HttpRequest(String url, int timeout, Map<String, String> header, String body, HttpListener<T> listener) {
        initialize(url, timeout, header, body, listener);
    }

    private void initialize(String url, int timeout, Map<String, String> header, String body, HttpListener<T> listener) {
        this.endpoint = url;
        this.timeout = timeout;
        this.header = header;
        this.body = body;
        this.listener = listener;
    }

    protected void request() {
        if (null == endpoint || "".equalsIgnoreCase(endpoint)) return;
        if (endpoint.startsWith("https")) {
            requestHTTPS();
        } else {
            requestHTTP();
        }
    }

    private void requestHTTP() {
        final StringBuffer response = new StringBuffer();
        HttpURLConnection conn = null;
        try {
            URL url = new URL(this.endpoint);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(Math.max(timeout * 4, MIN_TIMEOUT));
            conn.setConnectTimeout(Math.max(timeout, MIN_TIMEOUT));
            conn.setRequestMethod(method());
            conn.setDoOutput(output());
            enableJsonProperties(conn);
            enableHeader(conn);
            enableBody(conn);
            final int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                buildResponse(conn, response);
                successHandler(getResponseType(), response);
            } else {
                errorHandler(responseCode);
            }
        } catch (final JsonParseException ex) {
            errorHandler(ex);
        } catch (final MalformedURLException ex) {
            errorHandler(ex);
        } catch (final SocketTimeoutException ex) {
            errorHandler(ex);
        } catch (final IOException ex) {
            errorHandler(ex);
        } catch (final Exception ex) {
            errorHandler(ex);
        } finally {
            if (null != conn) conn.disconnect();
        }
    }

    private void requestHTTPS() {
        final StringBuffer response = new StringBuffer();
        HttpsURLConnection conn = null;
        try {
            URL url = new URL(this.endpoint);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setReadTimeout(Math.max(timeout * 4, MIN_TIMEOUT));
            conn.setConnectTimeout(Math.max(timeout, MIN_TIMEOUT));
            conn.setRequestMethod(method());
            conn.setDoOutput(output());
            enableJsonProperties(conn);
            enableHeader(conn);
            enableSSLOnApiBeforeLollipop(conn);
            enableBody(conn);
            final int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                buildResponse(conn, response);
                successHandler(getResponseType(), response);
            } else {
                errorHandler(responseCode);
            }
        } catch (final JsonParseException ex) {
            errorHandler(ex);
        } catch (final MalformedURLException ex) {
            errorHandler(ex);
        } catch (final SocketTimeoutException ex) {
            errorHandler(ex);
        } catch (final IOException ex) {
            errorHandler(ex);
        } catch (final Exception ex) {
            errorHandler(ex);
        } finally {
            if (null != conn) conn.disconnect();
        }
    }

    private void enableJsonProperties(HttpURLConnection conn) {
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("connection", "close");
    }

    private void enableBody(HttpURLConnection conn) throws IOException {
        if (null == body) return;
        OutputStream os = conn.getOutputStream();
        os.write(body.getBytes());
        os.flush();
        os.close();
    }

    private void buildResponse(HttpURLConnection conn, StringBuffer response) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
    }

    private boolean output() {
        return null != body;
    }

    private String method() {
        if (null == body) return "GET";
        return "POST";
    }

    private void enableHeader(HttpURLConnection conn) {
        if (header == null) return;
        for (Map.Entry<String, String> entry : header.entrySet()) {
            conn.setRequestProperty(entry.getKey(), entry.getValue());
        }
    }

    private void enableSSLOnApiBeforeLollipop(HttpsURLConnection conn) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < Build.VERSION_CODES.LOLLIPOP) {
            if (endpoint.startsWith("https")) {
                try {
                    TLSSocketFactory sc = new TLSSocketFactory();
                    conn.setSSLSocketFactory(sc);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void successHandler(Type responseType, StringBuffer response) {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (null == listener) return;
            if (null == responseType) {
                listener.onSuccess((T) response.toString());
            } else {
                final T jsonResponse = GsonHelper.newInstance().getGson().fromJson(response.toString().trim(), responseType);
                listener.onSuccess(jsonResponse);
            }
        });
    }

    private void errorHandler(Exception error) {
        if (null == listener) return;
        new Handler(Looper.getMainLooper()).post(() -> listener.onError(new Error(error)));
        Log.e(TAG, error.getMessage(), error);
    }

    private void errorHandler(int errorCode) {
        if (null == listener) return;
        new Handler(Looper.getMainLooper()).post(() -> listener.onError(new Error(errorCode)));
        Log.e(TAG, "error hit api code  ".concat(String.valueOf(errorCode)).concat(method()));
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
