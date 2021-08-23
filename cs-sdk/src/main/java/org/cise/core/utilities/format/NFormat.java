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
 */
public class NFormat {

    public static NumberFormat currency() {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.getDefault());
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        return nf;
//        return DecimalFormat.getInstance();
    }

    /**
     * remove currency symbol
     */
    public static String currencyFormat(double value) {
        NumberFormat format = currency();
        String result = format.format(value);
        return result.replace(format.getCurrency().getSymbol(), "");
    }

    public static String currencyFormatWithSymbol(double value) {
        NumberFormat format = currency();
        return format.format(value);
    }

    public static double currencyParse(String value) {
        DecimalFormat format = (DecimalFormat) currency();
        double result = 0;
        try {
            result = format.parse(value).doubleValue();
            return result;
        } catch (ParseException e) {
            // ignore
        }
        try {
            // remove currency symbol
            String pattern = format.toPattern().replaceAll("\u00A4", "");
            result = new DecimalFormat(pattern).parse(value).doubleValue();
            return result;
        } catch (ParseException e) {
            // ignore
        }
        return result;
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
            return "0".concat(result);
        }
        return result;
    }

    public static double percentParse(String value) throws ParseException {
        return percent().parse(value).doubleValue();
    }
}
