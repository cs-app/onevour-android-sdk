package org.cise.core.utilities.input;

import android.util.Log;

import org.cise.core.utilities.commons.ValueUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.concurrent.atomic.AtomicReference;

public class InputDouble implements NumberInputAdapter {

    private static final String TAG = "NID-DBL";

    private int decrease = 10;

    private AtomicReference<Double> value = new AtomicReference<Double>(0.00);

    private NumberFormat numberFormat;

    // cusrsor position after comma
    private int cursor = 0;

    private boolean isAfterPoint = false;

    private String groupSeparator = ",";

    private String decimalSeparator = ".";

    private double min = 0.00, max = Double.MAX_VALUE;

    public InputDouble(NumberFormat numberFormat, double min, double max) {
        this.numberFormat = numberFormat;
        this.min = min;
        this.max = max;
        DecimalFormatSymbols d = ((DecimalFormat) numberFormat).getDecimalFormatSymbols();
        decimalSeparator = String.valueOf(d.getDecimalSeparator());
        groupSeparator = String.valueOf(d.getGroupingSeparator());
    }

    @Override
    public String getValueString() {
        return numberFormat.format(value.get());
    }

    @Override
    public void setValue(String valueStr) throws ParseException {
        if (ValueUtils.isEmpty(valueStr)) {
            value.set(0.00);
        } else value.set(numberFormat.parse(valueStr.trim()).doubleValue());
    }

    @Override
    public void append(String valueChar) throws ParseException {
        Log.d(TAG, "input " + valueChar);
        if (valueChar.equalsIgnoreCase(decimalSeparator)) {
            isAfterPoint = true;
            cursor = 0;
            return;
        }
        try {
            String[] values = values();
            String left = values[0];
            String right = values[1];
            StringBuilder sb = new StringBuilder();
            if (isAfterPoint) {
                sb.append(left);
                sb.append('.');
                sb.append(appendRightValue(right, valueChar));
            } else {
                sb.append(appendLeftValue(left, valueChar));
                sb.append('.');
                sb.append(right);
            }
            Log.d(TAG, "final string " + sb.toString());
            double resultVal = Double.parseDouble(sb.toString());
            if (resultVal <= max) {
                value.set(resultVal);
            }
        } catch (NumberFormatException e) {
            // ignore max value
            Log.e(TAG, "max value input " + e.getMessage());
        }
    }

    private String[] values() {
        String firstNumberAsString = numberFormat.format(value.get());
        String[] values = firstNumberAsString.split("\\" + decimalSeparator);
        values[0] = values[0].replaceAll("\\" + groupSeparator, "");
        values[1] = values[1];
        Log.d(TAG, firstNumberAsString + " | l : " + values[0] + " | r : " + values[1]);
        return values;
    }

    private String appendLeftValue(String exist, String appender) {
        if (ValueUtils.isEmpty(exist) || exist.equalsIgnoreCase("0")) {
            return appender;
        }
        return exist + appender;
    }

    private String appendRightValue(String exist, String appender) {
        if (ValueUtils.isEmpty(exist) || exist.equalsIgnoreCase("0")) {
            return appender;
        }
        char[] values = exist.toCharArray();
        values[cursor] = appender.charAt(0);
        if (cursor < 1) cursor++;
        return String.valueOf(values);
    }

    @Override
    public void delete() {
        String[] values = values();
        String leftValue = values[0];
        String rightValue = values[1];
        // check right > 0
        int right = Integer.parseInt(rightValue);
        StringBuilder sb = new StringBuilder();
        if (right > 0) {
            sb.append(leftValue);
            sb.append('.');
            sb.append(deleteAfterComma(right));
        } else {
            sb.append(deleteBeforeComma());
        }
        value.set(Double.parseDouble(sb.toString()));
    }

    @Override
    public double getValueDouble() {
        return value.get();
    }

    @Override
    public int getValueInteger() {
        return value.get().intValue();
    }

    @Override
    public void setMaxValue() {
        value.set(max);
    }

    private double deleteBeforeComma() {
        double integer = value.get() / decrease;
        double diff = value.get() % decrease;
        if (diff > 0) integer = (value.get() - diff) / decrease;
        return integer;
    }

    private int deleteAfterComma(int exist) {
        Log.d(TAG, "cursor position " + cursor);
        if (cursor == 0) {
            isAfterPoint = false;
            return 0;
        }
        int integer = exist / decrease;
        if (integer == 1) return 0;
        int diff = exist % decrease;
        if (diff > 0) integer = (exist - diff) / decrease;
        if (cursor > 0) cursor--;
        return integer;
    }
}