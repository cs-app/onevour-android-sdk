/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cise.core.utilities.http;

/**
 *
 * @author user
 * @param <T>
 */
public class Response<T> {

    public final T result;
    public final Error error;

    private Response(T result) {
        if (result instanceof Error) {
            this.result = null;
            this.error = (Error) result;
        } else {
            this.result = result;
            this.error = null;
        }
    }

    public interface Listener<T> {

        void onSuccess(T response);

        void onError(Error error);

    }

}
