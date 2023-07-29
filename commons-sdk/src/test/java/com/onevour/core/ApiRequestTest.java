package com.onevour.core;

import com.onevour.core.utilities.http.HttpListener;
import com.onevour.core.utilities.http.HttpRequest;

public class ApiRequestTest extends HttpRequest {


    public ApiRequestTest(String url, HttpListener listener) {
        super(url, listener);
    }

    public ApiRequestTest(String url, int timeout, HttpListener listener) {
        super(url, timeout, listener);
    }

    protected ApiRequestTest(String url, String json, HttpListener listener) {
        super(url, json, listener);
    }

    protected ApiRequestTest(String url, int timeout, String json, HttpListener listener) {
        super(url, timeout, json, listener);
    }

    @Override
    public void request() {
        super.request();
    }
}
