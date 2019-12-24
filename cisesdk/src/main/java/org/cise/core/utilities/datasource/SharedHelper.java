package org.cise.core.utilities.datasource;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.cise.core.utilities.commons.ValueUtils;

public class SharedHelper {

    private Context context;

    private String preferenceKey = "application";

    public SharedHelper(Context context) {
        this.context = context;
    }

    private SharedPreferences getPreference() {
        return context.getSharedPreferences(preferenceKey, Context.MODE_PRIVATE);
    }

    private SharedPreferences.Editor getEditor() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceKey, Context.MODE_PRIVATE);
        return sharedPreferences.edit();
    }

    public <T> void setObject(String key, T value) {
        if (ValueUtils.nonNull(value)) {
            String stringValue = new Gson().toJson(value);
            setString(key, stringValue);
        }
    }


    public void setString(String key, String value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(key, value);
        editor.commit();
    }

    public void setInt(String key, int value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putInt(key, value);
        editor.commit();
    }

    public <T> T objectValue(String key, Class<T> classOf) {
        String value = stringValue(key);
        return new Gson().fromJson(value, classOf);
    }

    public String stringValue(String key) {
        return getPreference().getString(key, null);
    }

    public int intValue(String key) {
        return getPreference().getInt(key, 0);
    }


    public void remove(String key) {
        SharedPreferences.Editor editor = getEditor();
        editor.remove(key);
        editor.commit();
    }
}
