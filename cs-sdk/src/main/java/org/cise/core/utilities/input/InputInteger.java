package org.cise.core.utilities.input;

import org.cise.core.utilities.commons.ValueOf;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;

public class InputInteger implements NumberInputAdapter {

    private static final String TAG = "NID-INT";

    private AtomicInteger value = new AtomicInteger(0);

    private int decrease = 10;

    private int min, max;

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
        if (ValueOf.isEmpty(valueStr)) {
            value.set(0);
        } else value.set(Integer.parseInt(valueStr));
    }

    @Override
    public void append(String valueChar) {
        BigInteger integer = BigInteger.valueOf(value.intValue()).multiply(BigInteger.valueOf(decrease)).add(new BigInteger(valueChar));
        if (integer.compareTo(BigInteger.valueOf(max)) > 0) return;
        value.set(integer.intValue());
    }

    @Override
    public void delete() {
        int integer = (value.get() / decrease);
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
