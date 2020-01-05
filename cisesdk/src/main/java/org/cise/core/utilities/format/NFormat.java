package org.cise.core.utilities.format;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * Number Format<br/>
 * Currency<br/>
 * Percent<br/>
 * */
public class NFormat {

    public static NumberFormat currency() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setCurrencySymbol("");
        DecimalFormat nf = (DecimalFormat) DecimalFormat.getCurrencyInstance(Locale.getDefault());
        nf.setDecimalFormatSymbols(symbols);
        return nf;
    }

    public static String currencyFormat(double value) {
        return currency().format(value);
    }

    public static double currencyParse(String value) throws ParseException {
        return currency().parse(value).doubleValue();
    }

    public static NumberFormat percent() {
        NumberFormat format = NumberFormat.getNumberInstance(Locale.getDefault());
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        return format;
    }

    public static String percentFormat(double value) {
        String result = percent().format(value);
        if (1 > value && !result.startsWith("0")) {
            return "0" + result;
        }
        return result;
    }

    public static double percentParse(String value) throws ParseException {
        return percent().parse(value).doubleValue();
    }
}
