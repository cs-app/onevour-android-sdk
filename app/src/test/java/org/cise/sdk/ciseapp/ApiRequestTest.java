package org.cise.core;

import android.content.Context;

import org.cise.core.utilities.http.ApiRequest;
import org.cise.core.utilities.http.HttpRequest;
import org.cise.core.utilities.http.HttpResponse;

public class ApiRequestTest extends HttpRequest {


    public ApiRequestTest(Context context, String url, HttpResponse.Listener listener) {
        super(context, url, listener);
    }

    public ApiRequestTest(Context context, String url, int timeout, HttpResponse.Listener listener) {
        super(context, url, timeout, listener);
    }

    protected ApiRequestTest(Context context, String url, String json, HttpResponse.Listener listener) {
        super(context, url, json, listener);
    }

    protected ApiRequestTest(Context context, String url, int timeout, String json, HttpResponse.Listener listener) {
        super(context, url, timeout, json, listener);
    }

    @Override
    public void request() {
        super.request();
    }
}
