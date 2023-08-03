package com.onevour.core.utilities.input;

import java.text.NumberFormat;
import java.text.ParseException;

public interface NumberInputAdapter {

    String getValueString();

    void setValue(String value) throws ParseException;

    void append(String valueChar) throws ParseException;

    void delete();

    double getValueDouble();

    int getValueInteger();

    void setMaxValue();

    boolean isAfterPoint();

    void updateMinMax(double min, double max);
}
