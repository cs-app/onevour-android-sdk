package org.cise.core.utilities.commons;

import android.content.Context;
import android.content.SharedPreferences;

import org.cise.core.utilities.json.gson.GsonHelper;

public class RefSession {

    private final String TAG = getClass().getSimpleName();

    private final String EDITOR_DEFAULT = "CSD";

    private SharedPreferences getSharedPreferences(String Key) {
        return ContextHelper.getApplication().getSharedPreferences(Key, Context.MODE_PRIVATE);
    }

    private SharedPreferences.Editor editor(String Key) {
        SharedPreferences sharedPreferences = ContextHelper.getApplication().getSharedPreferences(Key, Context.MODE_PRIVATE);
        return sharedPreferences.edit();
    }

    /**
     * get
     */
    public <T> T find(Class<T> cls) {
        String key = cls.getSimpleName().toUpperCase();
        return find(key, cls);
    }

    public <T> T find(String key, Class<T> cls) {
        String valueString = getSharedPreferences(EDITOR_DEFAULT).getString(key, null);
        android.util.Log.d(TAG, "find : " + key + "\n" + valueString);
        if (null == valueString) return null;
        return GsonHelper.newInstance().getGson().fromJson(valueString, cls);
    }


    public int findInt(String key) {
        return getSharedPreferences(EDITOR_DEFAULT).getInt(key, 0);
    }

    public long findLong(String key) {
        return getSharedPreferences(EDITOR_DEFAULT).getLong(key, 0);
    }

    public double findDouble(String key) {
        return Double.parseDouble(getSharedPreferences(EDITOR_DEFAULT).getString(key, "0.0"));
    }

    public boolean findBoolean(String key) {
        return getSharedPreferences(EDITOR_DEFAULT).getBoolean(key, false);
    }

    public String findString(String key) {
        return getSharedPreferences(EDITOR_DEFAULT).getString(key, null);
    }


    /**
     * create or replace multiple
     */
    public void save(Object... values) {
        for (Object value : values) {
            String key = value.getClass().getSimpleName().toUpperCase();
            save(key, value, false);
        }
    }

    /**
     * create or replace single
     */
    public void save(Object value) {
        save(value.getClass().getSimpleName().toUpperCase(), value, false);
    }

    public void saveInt(String key, Integer value) {
        save(key, value, true);
    }

    public void saveLong(String key, Long value) {
        save(key, value, true);
    }

    public void saveDouble(String key, Double value) {
        save(key, value, true);
    }

    public void saveBoolean(String key, Boolean value) {
        save(key, value, true);
    }

    public void save(String key, Object value) {
        save(key, value, false);
    }

    public void save(String key, Object value, boolean isNative) {
        if (null == value || null == key || key.trim().isEmpty()) {
            throw new NullPointerException("cannot store null object, key and empty key");
        }
        android.util.Log.d(TAG, "set object with key: " + key);
        SharedPreferences.Editor editor = editor(EDITOR_DEFAULT);
        if (isNative) {
            if (value instanceof Integer) {
                editor.putInt(key, (int) value);
            } else if (value instanceof Float) {
                editor.putFloat(key, (float) value);
            } else if (value instanceof Long) {
                editor.putLong(key, (long) value);
            } else if (value instanceof Double) {
                editor.putString(key, String.valueOf(value));
            } else if (value instanceof Boolean) {
                editor.putBoolean(key, (boolean) value);
            } else if (value instanceof String) {
                editor.putString(key, (String) value);
            } else
                throw new IllegalArgumentException("Only int, float, boolean, string acceptable when native set true");
        } else {
            String valueString = GsonHelper.newInstance().getGson().toJson(value);
            editor.putString(key, valueString);
        }
        if (!editor.commit()) {
            throw new IllegalStateException("cannot commit save keys " + key);
        }
    }

    public <T> void delete(Class<T> cls) {
        String key = cls.getSimpleName().toUpperCase();
        delete(key);
    }

    public void delete(Object... objs) {
        for (Object o : objs) {
            delete(o.getClass().getSimpleName().toUpperCase());
        }
    }

    public void delete(String... keys) {
        SharedPreferences.Editor editor = editor(EDITOR_DEFAULT);
        StringBuilder sb = new StringBuilder("|");
        for (String key : keys) {
            if (null == key || key.trim().isEmpty()) {
                throw new NullPointerException("cannot remove empty key");
            }
            sb.append(key).append("|");
            editor.remove(key);
            android.util.Log.d(TAG, "remove " + key);
        }
        if (!editor.commit()) {
            throw new IllegalStateException("cannot commit remove keys " + sb.toString());
        }
    }
}
