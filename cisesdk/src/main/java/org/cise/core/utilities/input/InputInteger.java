package org.cise.core.utilities.input;

import android.util.Log;

import org.cise.core.utilities.commons.ValueUtils;

import java.util.concurrent.atomic.AtomicReference;

public class InputInteger implements NumberInputAdapter {

    private static final String TAG = "NID-INT";

    private int decrease = 10;

    private AtomicReference<Integer> value = new AtomicReference<>(0);

    private int min = 0, max = Integer.MAX_VALUE;

    public InputInteger(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public String getValueString() {
        return String.valueOf(value);
    }

    @Override
    public void setValue(String valueStr) {
        if (ValueUtils.isEmpty(valueStr)) {
            value.set(0);
        } else value.set(Integer.parseInt(valueStr));
    }

    @Override
    public void append(String valueChar) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(value.get().intValue());
            sb.append(valueChar);
            int resultVal = Integer.parseInt(sb.toString());
            if (resultVal <= max) {
                value.set(resultVal);
            }
        } catch (NumberFormatException e) {
            // ignore max value
            Log.e(TAG, "max value input " + e.getMessage());
        }
    }

    @Override
    public void delete() {
        int integer = value.get() / decrease;
        int diff = value.get() % decrease;
        if (diff > 0) integer = (value.get() - diff) / decrease;
        value.set(integer);
    }

    @Override
    public double getValueDouble() {
        return value.get();
    }

    @Override
    public int getValueInteger() {
        return value.get();
    }

    @Override
    public void setMaxValue() {
        value.set(max);
    }
}
