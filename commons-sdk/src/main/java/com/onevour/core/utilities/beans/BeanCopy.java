package com.onevour.core.utilities.beans;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.onevour.core.utilities.commons.ValueOf;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings({"CollectionAddAllCanBeReplacedWithConstructor", "unchecked", "rawtypes"})
public class BeanCopy {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static <T> T value(Object source, Class<T> target, String... ignore) {
        if (ValueOf.isNull(source)) throw new NullPointerException();
        try {
            Constructor constructor = target.getConstructor();
            T newInstance = (T) constructor.newInstance();
            copyValue(source, newInstance, ignore);
            return newInstance;
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException |
                 InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> values(List source, Class<T> target, String... ignore) {
        List<T> result = new ArrayList<>();
        if (ValueOf.isNull(source)) return result;
        if (source.isEmpty()) return result;
        for (Object o : source) {
            result.add(value(o, target, ignore));
        }
        return result;
    }

    public static <T> void copyValue(Object source, T target, String... ignore) {
        Set<String> ignoreSet = new HashSet<>();
        ignoreSet.addAll(Arrays.asList(ignore));
        Class<?> clazzTarget = target.getClass();
        Class<?> clazzSource = source.getClass();
        List<Field> fieldTargets = getAllModelFields(clazzTarget);
        List<Field> fieldSources = getAllModelFields(clazzSource);
        for (Field field : fieldSources) {
            if (ignoreSet.contains(field.getName())) continue;
            for (Field fieldTarget : fieldTargets) {
                if (!field.getName().equalsIgnoreCase(fieldTarget.getName())) continue;
                try {
                    field.setAccessible(true);
                    fieldTarget.setAccessible(true);
                    fieldTarget.set(target, field.get(source));
                    break;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static <T> void copy(T source, T target, String... ignore) {
        Set<String> ignoreSet = new HashSet<>();
        ignoreSet.addAll(Arrays.asList(ignore));
        Class<T> clazz = (Class<T>) source.getClass();
        List<Field> fields = getAllModelFields(clazz);
        for (Field field : fields) {
            if (ignoreSet.contains(field.getName())) continue;
            try {
                field.setAccessible(true);
                field.set(target, field.get(source));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    protected static List<Field> getAllModelFields(Class aClass) {
        List<Field> fields = new ArrayList<>();
        do {
            Collections.addAll(fields, aClass.getDeclaredFields());
            aClass = aClass.getSuperclass();
        } while (aClass != null);
        return fields;
    }
}
