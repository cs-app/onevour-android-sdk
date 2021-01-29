package org.cise.core.utilities.input;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;

import org.cise.core.utilities.commons.ExceptionUtils;
import org.cise.core.utilities.commons.ValueUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by zuliadin on 08/10/2016.
 */
public class NumberInputV1 implements View.OnTouchListener, NumberInputGUI.AlertListener {

    private static final String TAG = "NID";

    private Context context;

    private EditText editText;

    private StringBuilder resValueTmp = new StringBuilder();

    private double maxDbl;

    private int maxInt;

    private boolean isPoint = false, isAfterPoint = false;

    private NumberFormat numberFormat;

    private String decimalSeparator = ".";

    private String decimalGroupSeparator = ",";

    private Listener listener;

    private AtomicReference<Integer> valueInteger = new AtomicReference<>(0);

    private NumberInputGUI alert = new NumberInputGUI();

    private NumberInputAdapter adapter;

    public NumberInputV1() {

    }

    public NumberInputV1(final EditText editText) {
        setup(editText);
    }

    public NumberInputV1(final EditText editText, final NumberFormat numberFormat, double min, double max) {
        setup(editText, numberFormat, min, max);
    }

    public void setup(final EditText editText) {
        setup(editText, null, 0, Integer.MAX_VALUE);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setup(@NonNull EditText editText, NumberFormat numberFormat, double min, double max) {
        this.context = editText.getRootView().getContext();
        this.editText = editText;
        this.editText.setTextIsSelectable(true);
        this.editText.setCursorVisible(false);
        this.editText.setFocusable(false);
        this.editText.setOnTouchListener(this);
        this.numberFormat = numberFormat;
        alert.init(context, numberFormat, min, max, this);
        // check decimal
        String tmpValue = this.editText.getText().toString().trim();
        if (tmpValue.isEmpty()) {
            if (isDecimal()) {
                editText.setText(numberFormat.format(0.00));
            } else {
                editText.setText("0");
            }
        }
        if (isDecimal()) {
            DecimalFormatSymbols d = ((DecimalFormat) this.numberFormat).getDecimalFormatSymbols();
            decimalSeparator = String.valueOf(d.getDecimalSeparator());
            decimalGroupSeparator = String.valueOf(d.getGroupingSeparator());
//            numPoint.setText(decimalSeparator);
//            alert.setDecimalSeparator(decimalSeparator);
            maxDbl = max;
            resValueTmp = new StringBuilder(String.valueOf(min));
        } else {
            int minInt = (int) min;
            maxInt = (int) max;
            resValueTmp = new StringBuilder(String.valueOf(minInt));
//            alert.setDecimalSeparator(null);
        }
    }

    private boolean isDecimal() {
        return null != numberFormat;
    }

    private void yesButton() {
        try {
            Log.d(TAG, "Listener is available : " + listener);
            if (ValueUtils.nonNull(listener)) listener.onSubmitValue();
            if (ValueUtils.nonNull(numberFormat)) {
                if (isDecimal()) {
                    //editText.setText(result.getText());
                } else {
                    editText.setText(numberFormat.format(numberFormat.parse(resValueTmp.toString()).intValue()));
                }
            } else {
                editText.setText(resValueTmp);
            }
            if (ValueUtils.nonNull(listener)) {
                if (isDecimal()) {
                    listener.doubleValue(numberFormat.parse(resValueTmp.toString()).doubleValue());
                } else {
                    if (ValueUtils.nonNull(numberFormat)) {
                        listener.intValue(numberFormat.parse(resValueTmp.toString()).intValue());
                    } else {
                        listener.intValue(Integer.parseInt(resValueTmp.toString()));
                    }
                }
            }
        } catch (ParseException e) {
            Log.e(TAG, ExceptionUtils.message(e));
        }
    }

    public void setScrollFlag(boolean scroll) {
//        isScroll = scroll;
    }

    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    @SuppressLint("ClickableViewAccessibility")
    public void disableTouch() {
        editText.setOnTouchListener(null);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void enableTouch() {
        editText.setOnTouchListener(this::onTouch);
    }

    public void setTitle(String left, String right) {
//        titleContent.setVisibility(View.VISIBLE);
//        titleLeft.setText(left);
//        titleRight.setText(right);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (ValueUtils.nonNull(imm)) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

        if (v.getId() == editText.getId() && motionEvent.getAction() == MotionEvent.ACTION_UP) {
            Log.d(TAG, "Action touch event : " + motionEvent.getAction());
            if (isDecimal()) {

            } else {
                valueInteger.set(ValueUtils.isEmpty(editText.getText().toString()) ? 0 : Integer.parseInt(editText.getText().toString()));
                alert.setResult(String.valueOf(valueInteger.get()));
            }
            alert.show();
        }
        return false;
    }

    @Override
    public void inputValue(String valueChar) throws ParseException {
        if (isDecimal()) {
            inputDouble(valueChar);
        } else inputInteger(valueChar);
    }

    @Override
    public void showMaxValue() {

    }

    // integer only
    private void inputInteger(String valueChar) throws ParseException {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(valueInteger.get().intValue());
            sb.append(valueChar);
            valueInteger.set(Integer.parseInt(sb.toString()));
            alert.setResult(String.valueOf(valueInteger.get().intValue()));
        } catch (NumberFormatException e) {
            // ignore max value
            Log.e(TAG, "max value input " + e.getMessage());
        }
    }

    // double
    private void inputDouble(String valueChar) {
        if (valueChar.equalsIgnoreCase(decimalSeparator)) {
            isPoint = true;
            isAfterPoint = true;
        } else {
            BigDecimal value = getAvailableValue();
            String[] partValue = String.valueOf(value).split("\\.");
            StringBuilder sbBfPoint = new StringBuilder(partValue[0]);
            StringBuilder sbAfPoint = new StringBuilder(partValue[1]);
            if (!isPoint) {
                sbBfPoint.append(valueChar);
                sbBfPoint.append(".");
                sbBfPoint.append(sbAfPoint);
            } else {
                if (isAfterPoint) {
                    if (getAfterComma(value) == 0) {
                        sbAfPoint = new StringBuilder();
                        sbAfPoint.append(valueChar);
                    } else {
                        if (sbAfPoint.length() < 2) {
                            sbAfPoint.append(valueChar);
                        } else {
                            sbAfPoint.replace(sbAfPoint.length() - 1, sbAfPoint.length(), valueChar);
                        }
                    }
                    sbBfPoint.append(".");
                    sbBfPoint.append(sbAfPoint);
                    Log.i(TAG, sbBfPoint.toString());
                }
            }
            resValueTmp = new StringBuilder(sbBfPoint);
            printValueToUI(value);
        }
    }

    @Override
    public void delete() throws ParseException {
        if (isDecimal()) {
            BigDecimal value = getAvailableValue();
            String[] partValue = String.valueOf(value).split("[.]");
            StringBuilder sbBfPoint = new StringBuilder(partValue[0]);
            StringBuilder sbAfPoint = new StringBuilder(partValue[1]);
            if (getAfterComma(value) > 0) {
                int length = sbAfPoint.length();
                for (int i = length; i > 0; i--) {
                    if (!String.valueOf(sbAfPoint.charAt(i - 1)).equalsIgnoreCase("0")) {
                        sbAfPoint.delete(i - 1, i);
                        break;
                    }
                }
                if (sbAfPoint.length() == 0) {
                    sbAfPoint.append("0");
                    isAfterPoint = false;
                    isPoint = false;
                }
            } else {
                isAfterPoint = false;
                isPoint = false;
                int length = sbBfPoint.length();
                for (int i = length; i > 0; i--) {
                    sbBfPoint.delete(i - 1, i);
                    break;
                }
                length = sbBfPoint.length();
                if (length == 0) {
                    sbBfPoint.append("0");
                }
            }
            sbBfPoint.append(".");
            sbBfPoint.append(sbAfPoint);
            resValueTmp = new StringBuilder(sbBfPoint);
            printValueToUI(value);
        } else {
            int decrease = 10;
            int integer = valueInteger.get() / decrease;
            int diff = valueInteger.get() % decrease;
            if (diff > 0) integer = (valueInteger.get() - diff) / decrease;
            valueInteger.set(integer);
            alert.setResult(String.valueOf(valueInteger.get()));
        }
    }

    @Override
    public void submit() {
        editText.setText(String.valueOf(valueInteger.get()));
    }

    @Deprecated
    private BigInteger getAvailableIntValue() throws ParseException {
        String value = resValueTmp.toString();
        if (ValueUtils.isEmpty(value)) {
            value = "0";
        }
        return BigInteger.valueOf(Long.parseLong(value));
    }

    private BigDecimal getAvailableValue() {
        double value = 0.00;
        if (ValueUtils.nonNull(numberFormat)) {
//            String numberString = result.getText().toString();
//            resValueTmp = new StringBuilder(numberString);
            value = currencyParse(resValueTmp.toString());
        } else {
            if (!resValueTmp.toString().trim().isEmpty()) {
                value = Double.parseDouble(resValueTmp.toString());
            }
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        Log.i(TAG, "get value : " + bd);
        return bd;
    }

    private int getAfterComma(BigDecimal a) {
        String[] result = String.valueOf(a).split("\\.");
        if (result.length > 1) {
            return Integer.parseInt(result[1]);
        } else {
            return 0;
        }
    }

    @Deprecated
    private void printValueToUI(BigDecimal value) {
        if (Double.parseDouble(resValueTmp.toString()) <= maxDbl) {
            if (ValueUtils.nonNull(numberFormat)) {
//                result.setText(currencyFormat(Double.parseDouble(resValueTmp.toString())));
            } else {
//                result.setText(String.valueOf(Double.valueOf(resValueTmp.toString())));
            }
//            resValueTmp = new StringBuilder(result.getText().toString());
        } else {
            resValueTmp = new StringBuilder(String.valueOf(value));
        }
    }

    @Deprecated
    private void printValueToUI(BigInteger value) {
        Log.i(TAG, "PRINT : " + resValueTmp);
        if (Long.parseLong(resValueTmp.toString()) <= maxInt) {
            if (ValueUtils.nonNull(numberFormat)) {
                //result.setText(numberFormat.format(Integer.valueOf(resValueTmp.toString())));
            } else {
                //result.setText(String.valueOf(Integer.valueOf(resValueTmp.toString())));
            }
            //resValueTmp = new StringBuilder(result.getText().toString());
        } else {
            resValueTmp = new StringBuilder(String.valueOf(value));
        }
    }

    public String currencyFormat(double value) {
        String result = numberFormat.format(value);
        return result.replace(numberFormat.getCurrency().getSymbol(), "");
    }

    public double currencyParse(String value) {
        DecimalFormat format = (DecimalFormat) numberFormat;
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

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {

        void onSubmitValue();

        void doubleValue(double v);

        void intValue(int i);

    }
}
