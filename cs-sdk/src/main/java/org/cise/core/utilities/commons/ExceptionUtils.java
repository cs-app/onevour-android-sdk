package org.cise.core.utilities.commons;
//
// Created by Zuliadin on 2019-12-24.
//

public class ExceptionUtils {

    public static String message(Exception e) {
        StringBuilder sb = new StringBuilder(e.getCause().getMessage());
        sb.append("\n");
        for (StackTraceElement st : e.getStackTrace()) {
            sb.append(st);
        }
        return sb.toString();
    }
}
