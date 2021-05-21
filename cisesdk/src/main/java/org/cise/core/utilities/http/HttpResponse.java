/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cise.core.utilities.http;

/**
 * @param <T>
 * @author Zuliadin
 */
public class HttpResponse<T> {

    public final T result;

    public final HttpError httpError;

    private HttpResponse(T result) {
        if (result instanceof HttpError) {
            this.result = null;
            this.httpError = (HttpError) result;
        } else {
            this.result = result;
            this.httpError = null;
        }
    }

    public interface Listener<T> {

        void onSuccess(T response);

        void onError(HttpError httpError);

    }

}
