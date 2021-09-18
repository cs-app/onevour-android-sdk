package org.cise.core.utilities.commons;
//
// Created by Zuliadin on 2019-12-24.
//

public class ValueOf {

    public static boolean isNull(Object value) {
        return null == value;
    }

    public static boolean isNull(Object... values) {
        for (Object o : values) {
            if (isNull(o)) return true;
        }
        return false;
    }

    public static boolean isEmpty(String value) {
        if (isNull(value)) return true;
        return value.trim().isEmpty();
    }

    public static boolean isEmpty(String... value) {
        for (String o : value) {
            if (isEmpty(o)) return true;
        }
        return false;
    }

    public static boolean isZero(int value) {
        return 0 == value;
    }

    public static boolean isZero(int... value) {
        for (int o : value) {
            if (isZero(o)) return true;
        }
        return false;
    }

    public static boolean nonEmpty(String value) {
        return !isEmpty(value);
    }

    public static boolean nonEmpty(String... value) {
        return !isEmpty(value);
    }

    public static boolean nonZero(int value) {
        return !isZero(value);
    }

    public static boolean nonZero(int... value) {
        return !isZero(value);
    }

    public static boolean nonNull(Object value) {
        return !isNull(value);
    }

    public static boolean nonNull(Object... values) {
        return !isNull(values);
    }


}
