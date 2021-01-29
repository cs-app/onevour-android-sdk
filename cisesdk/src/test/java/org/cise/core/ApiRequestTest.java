package org.cise.core;

import android.content.Context;

import org.cise.core.utilities.http.ApiRequest;
import org.cise.core.utilities.http.HttpRequest;
import org.cise.core.utilities.http.HttpResponse;

public class ApiRequestTest extends HttpRequest {


    public ApiRequestTest(String url, HttpResponse.Listener listener) {
        super(url, listener);
    }

    public ApiRequestTest(String url, int timeout, HttpResponse.Listener listener) {
        super(url, timeout, listener);
    }

    protected ApiRequestTest(String url, String json, HttpResponse.Listener listener) {
        super(url, json, listener);
    }

    protected ApiRequestTest(String url, int timeout, String json, HttpResponse.Listener listener) {
        super(url, timeout, json, listener);
    }

    @Override
    public void request() {
        super.request();
    }
}
