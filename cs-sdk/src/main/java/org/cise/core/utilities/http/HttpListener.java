package org.cise.core.utilities.http;

public interface HttpListener<T> {

    void onSuccess(T response);

    void onError(HttpError httpError);

}
