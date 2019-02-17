package org.cise.core.utilities.format;


import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class NFormat {

    public final static Locale ALC_LOCAL = new Locale("in", "ID");
    public final static String paternDFGeneral = "###,##0.00";
    public final static String paternNFGeneralComma = "###,##0.##";
    public final static NumberFormat NF_LOCALE = NumberFormat.getCurrencyInstance(ALC_LOCAL);

    // decimal
    public static DecimalFormatSymbols symbols = new DecimalFormatSymbols();

    public static NumberFormat DF_GENERAL = new DecimalFormat(paternDFGeneral);
    public static NumberFormat NF_GENERAL_COMMA = new DecimalFormat(paternNFGeneralComma);

    public static NumberFormat percentFormat = NumberFormat.getPercentInstance();

    // set get
    public static DecimalFormatSymbols getSymbol() {
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');
        return symbols;
    }

    public static NumberFormat getDecimalFormatSymbol() {
        return new DecimalFormat(paternDFGeneral, getSymbol());
    }

    public static NumberFormat getNumberDecimalFormatSymbol() {
        return new DecimalFormat(paternDFGeneral);
    }

    public static NumberFormat getNumberPercent() {
        DecimalFormatSymbols symbolPercent = new DecimalFormatSymbols();
        symbolPercent.setDecimalSeparator('.');
        symbolPercent.setGroupingSeparator(',');
        return new DecimalFormat(paternDFGeneral, symbolPercent);
    }

    public static NumberFormat getNumberCommaFormatSymbol() {
        return new DecimalFormat(paternNFGeneralComma, getSymbol());
    }

    public static NumberFormat getPercentFormat() {
        percentFormat.setMaximumFractionDigits(2);
        return percentFormat;
    }
}
