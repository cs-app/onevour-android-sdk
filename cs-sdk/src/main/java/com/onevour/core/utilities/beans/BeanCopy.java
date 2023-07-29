package com.onevour.core.utilities.beans;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BeanCopy {

    private final static String TAG = BeanCopy.class.getSimpleName();

    public static <T> void copyAllFields(T target, T from, String... ignore) {
        Set<String> ignoreSet = new HashSet<>();
        ignoreSet.addAll(Arrays.asList(ignore));

        Class<T> clazz = (Class<T>) from.getClass();
        List<Field> fields = getAllModelFields(clazz);

        if (fields == null) return;
        for (Field field : fields) {
            if (ignoreSet.contains(field.getName())) continue;
            try {
                field.setAccessible(true);
                field.set(target, field.get(from));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<Field> getAllModelFields(Class aClass) {
        List<Field> fields = new ArrayList<>();
        do {
            Collections.addAll(fields, aClass.getDeclaredFields());
            aClass = aClass.getSuperclass();
        } while (aClass != null);
        return fields;
    }
}
