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

    private final int MIN_TIMEOUT = 9000;

    private int timeout = 0;

    private String endpoint;

    private Map<String, String> header;

    private String body;

    private HttpResponse.Listener<T> listener;

    // GET
    public HttpRequest(String url, HttpResponse.Listener<T> listener) {
        initialize(url, MIN_TIMEOUT, null, body, listener);
    }

    // GET
    public HttpRequest(String url, int timeout, HttpResponse.Listener<T> listener) {
        initialize(url, timeout, null, body, listener);
    }

    // GET
    public HttpRequest(String url, Map<String, String> header, HttpResponse.Listener<T> listener) {
        initialize(url, MIN_TIMEOUT, header, body, listener);
    }

    // GET
    public HttpRequest(String url, int timeout, Map<String, String> header, HttpResponse.Listener<T> listener) {
        initialize(url, timeout, header, body, listener);
    }

    // POST
    protected HttpRequest(String url, String body, HttpResponse.Listener<T> listener) {
        initialize(url, MIN_TIMEOUT, null, body, listener);
    }

    // POST
    protected HttpRequest(String url, int timeout, String body, HttpResponse.Listener<T> listener) {
        initialize(url, timeout, null, body, listener);
    }

    // POST
    protected HttpRequest(String url, Map<String, String> header, String body, HttpResponse.Listener<T> listener) {
        initialize(url, MIN_TIMEOUT, header, body, listener);
    }

    // POST
    protected HttpRequest(String url, int timeout, Map<String, String> header, String body, HttpResponse.Listener<T> listener) {
        initialize(url, timeout, header, body, listener);
    }

    private void initialize(String url, int timeout, Map<String, String> header, String body, HttpResponse.Listener<T> listener) {
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
                // read response
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
        new Handler(Looper.getMainLooper()).post(() -> listener.onError(new HttpError(error)));
        Log.e(TAG, error.getMessage(), error);
    }

    private void errorHandler(int errorCode) {
        if (null == listener) return;
        new Handler(Looper.getMainLooper()).post(() -> listener.onError(new HttpError(errorCode)));
        Log.e(TAG, "error hit api code " + errorCode + " " + method() + " " + endpoint);
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

    /*
    protected void requestBAK() {
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
    */
}
