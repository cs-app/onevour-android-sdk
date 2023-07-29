package com.onevour.core.utilities.http;

public interface HttpListener<T> {

    void onSuccess(T response);

    void onError(Error error);

}
