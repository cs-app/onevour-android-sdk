package com.onevour.core.utilities.commons;

import android.text.Editable;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;

public class UIValue extends ValueOf {

    public static String string(EditText editText) {
        Editable editable = editText.getText();
        if (null == editable) return null;
        return editable.toString().trim();
    }

    public static String string(TextInputEditText editText) {
        Editable editable = editText.getText();
        if (null == editable) return null;
        return editable.toString().trim();
    }

    /**
     * return default empty string if null
     *
     * @Param value
     */
    public static String stringDefEmpty(String value) {
        if (value == null) return "";
        return value;
    }

    public static boolean isEmpty(EditText... editTexts) {
        for (EditText o : editTexts) {
            if (isEmpty(string(o))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEmpty(TextInputEditText... editTexts) {
        for (TextInputEditText o : editTexts) {
            if (isEmpty(string(o))) {
                return true;
            }
        }
        return false;
    }

    public static void clearError(TextInputEditText... editTexts) {
        for (TextInputEditText o : editTexts) {
            o.setError(null);
        }
    }
}
