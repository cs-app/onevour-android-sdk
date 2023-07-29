package com.onevour.core.utilities.input;

import android.util.Log;

import com.onevour.core.utilities.commons.ValueOf;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.concurrent.atomic.AtomicReference;

public class InputDouble implements NumberInputAdapter {

    private static final String TAG = "NID-DBL";

    private int decrease = 10;

    private AtomicReference<BigDecimal> value = new AtomicReference<>();

    private NumberFormat numberFormat;

    private int cursor = 0;

    private boolean isAfterPoint = false;

    private String decimalSeparator = ".";

    private double min = 0.00, max = Double.MAX_VALUE;

    public InputDouble(NumberFormat numberFormat, double min, double max) {
        value.set(BigDecimal.valueOf(0.00));
        this.numberFormat = numberFormat;
        this.min = min;
        this.max = max;
        DecimalFormatSymbols d = ((DecimalFormat) numberFormat).getDecimalFormatSymbols();
        decimalSeparator = String.valueOf(d.getDecimalSeparator());
    }

    @Override
    public String getValueString() {
        return numberFormat.format(value.get());
    }

    @Override
    public void setValue(String valueStr) throws ParseException {
        if (ValueOf.isEmpty(valueStr)) {
            value.set(BigDecimal.valueOf(0.00));
        } else value.set(BigDecimal.valueOf(numberFormat.parse(valueStr.trim()).doubleValue()));
    }

    @Override
    public void append(String valueChar) throws ParseException {
        Log.d(TAG, "input ".concat(valueChar));
        if (valueChar.equalsIgnoreCase(decimalSeparator)) {
            isAfterPoint = true;
            cursor = 0;
            return;
        }
        if (isAfterPoint) {
            appendComma(valueChar);
        } else {
            BigDecimal decimal = value.get().multiply(BigDecimal.valueOf(decrease)).add(new BigDecimal(valueChar));
            if (decimal.compareTo(BigDecimal.valueOf(max)) > 0) return;
            value.set(decimal);
        }
    }

    @Override
    public void delete() {
        BigDecimal fractionalPart = value.get().remainder(BigDecimal.ONE);
        if (fractionalPart.compareTo(BigDecimal.valueOf(0.00)) > 0) {
            deleteComma(fractionalPart);
        } else {
            BigDecimal decimal = value.get().divide(BigDecimal.valueOf(decrease), 2, RoundingMode.CEILING);
            BigDecimal diff = value.get().remainder(BigDecimal.valueOf(decrease));
            if (diff.compareTo(BigDecimal.valueOf(0.00)) > 0) {
                decimal = value.get().subtract(diff).divide(BigDecimal.valueOf(decrease), 2, RoundingMode.CEILING);
            }
            if (decimal.compareTo(BigDecimal.valueOf(max)) > 0) return;
            value.set(decimal);
        }
    }

    private void appendComma(String valueChar) {
        BigDecimal fractionalPart = value.get().remainder(BigDecimal.ONE, new MathContext(3));
        String fractionalPartStr = fractionalPart.toString();
        if (fractionalPartStr.length() == 3) fractionalPartStr = fractionalPartStr.concat("0");
        char[] values = fractionalPartStr.toCharArray();
        values[cursor + 2] = valueChar.charAt(0);
        if (cursor == 0) cursor++;
        BigDecimal decimal = value.get().subtract(fractionalPart).add(new BigDecimal(new String(values)));
        if (decimal.compareTo(BigDecimal.valueOf(max)) > 0) return;
        value.set(decimal);
    }

    private void deleteComma(BigDecimal fractionalPart) {
        char[] values = fractionalPart.toString().toCharArray();
        values[cursor + 2] = '0';
        isAfterPoint = !(cursor == 0);
        if (cursor == 1) cursor--;
        BigDecimal decimal = value.get().subtract(fractionalPart).add(new BigDecimal(new String(values)));
        value.set(decimal);
    }

    @Override
    public double getValueDouble() {
        return value.get().doubleValue();
    }

    @Override
    public int getValueInteger() {
        return value.get().intValue();
    }

    @Override
    public void setMaxValue() {
        value.set(BigDecimal.valueOf(max));
    }
}