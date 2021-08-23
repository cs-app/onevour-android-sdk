package org.cise.core.utilities.input;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by Zuliadin on 05/09/2016.
 */
public class TextViewFilter {

    public static class ValueNumber implements TextWatcher {

        private EditText editText;

        private boolean firstZero = true;

        public ValueNumber(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            firstZero = (s.toString().equalsIgnoreCase("") || s.toString().equalsIgnoreCase("0"));
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().startsWith("0")) {
                if (s.toString().length() >= 2) {
                    s.replace(0, 1, "");
                }
            } else if (s.toString().equalsIgnoreCase("")) {
                s.replace(0, 0, "0");
            } else if (firstZero) {
                if (s.toString().length() == 2) {
                }
            }
        }
    }


    public static class InputFilterMinMax implements InputFilter {

        private int min, max;

        public InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public InputFilterMinMax(String min, String max) {
            this.min = Integer.parseInt(min);
            this.max = Integer.parseInt(max);
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                if (!source.toString().equalsIgnoreCase("")) {
                    int input = Integer.parseInt(dest.toString().concat(source.toString()));
                    if (isInRange(min, max, input)) {
                        return null;
                    }
                }
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }
            return "";
        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }
}