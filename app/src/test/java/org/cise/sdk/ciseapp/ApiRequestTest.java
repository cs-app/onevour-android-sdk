package org.cise.core;

import android.content.Context;

import org.cise.core.utilities.http.HttpListener;
import org.cise.core.utilities.http.HttpRequest;

public class ApiRequestTest extends HttpRequest {


    public ApiRequestTest(Context context, String url, HttpListener listener) {
        super(context, url, listener);
    }

    public ApiRequestTest(Context context, String url, int timeout, HttpListener listener) {
        super(context, url, timeout, listener);
    }

    protected ApiRequestTest(Context context, String url, String json, HttpListener listener) {
        super(context, url, json, listener);
    }

    protected ApiRequestTest(Context context, String url, int timeout, String json, HttpListener listener) {
        super(context, url, timeout, json, listener);
    }

    @Override
    public void request() {
        super.request();
    }
}
