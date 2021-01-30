package org.cise.core.utilities.input;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.cise.core.R;
import org.cise.core.utilities.commons.ExceptionUtils;
import org.cise.core.utilities.commons.ValueUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Created by zuliadin on 08/10/2016.
 */
public class NumberInputBAK implements View.OnClickListener, View.OnTouchListener {

    private static final String TAG = "NID";

    private Context context;

    private AlertDialog alertDialog;

    private EditText editText;

    private TextView result;

    private StringBuilder resValueTmp;

    private int editTextId = 0;

    private double maxDbl;

    private int maxInt;

    private boolean isDecimal = false, isPoint = false, isAfterPoint = false;

    private NumberFormat numberFormat;

    private String decimalSeparator = ".";

    private String decimalGroupSeparator = ",";

    private LinearLayout titleContent;

    private TextView titleLeft, titleRight;

    private Listener listener;

    private boolean isScroll = false;

    private NumberInputAdapter adapter;

    public NumberInputBAK() {

    }

    public NumberInputBAK(final EditText editText) {
        setup(editText);
    }

    public NumberInputBAK(final EditText editText, final NumberFormat numberFormat, double min, double max) {
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
        this.isDecimal = null == numberFormat;
        this.numberFormat = numberFormat;
        // check decimal
        String tmpValue = editText.getText().toString();
        if (tmpValue.isEmpty()) {
            if (isDecimal) {
                editText.setText(numberFormat.format(0.00));
            } else {
                editText.setText("0");
            }
        }
        if (ValueUtils.nonNull(this.numberFormat) && this.numberFormat instanceof DecimalFormat) {
            DecimalFormatSymbols d = ((DecimalFormat) this.numberFormat).getDecimalFormatSymbols();
            decimalSeparator = String.valueOf(d.getDecimalSeparator());
            decimalGroupSeparator = String.valueOf(d.getGroupingSeparator());
        }
        if (isDecimal) {
            maxDbl = max;
            resValueTmp = new StringBuilder(String.valueOf(min));
        } else {
            int minInt = (int) min;
            maxInt = (int) max;
            resValueTmp = new StringBuilder(String.valueOf(minInt));
        }
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.input_number_dialog, null, false);
        titleContent = dialogView.findViewById(R.id.title_content);
        titleContent.setVisibility(View.GONE);
        titleLeft = dialogView.findViewById(R.id.title_left);
        titleRight = dialogView.findViewById(R.id.title_right);
        result = dialogView.findViewById(R.id.key_result);
        TextView num0 = dialogView.findViewById(R.id.key_num_0);
        TextView num1 = dialogView.findViewById(R.id.key_num_1);
        TextView num2 = dialogView.findViewById(R.id.key_num_2);
        TextView num3 = dialogView.findViewById(R.id.key_num_3);
        TextView num4 = dialogView.findViewById(R.id.key_num_4);
        TextView num5 = dialogView.findViewById(R.id.key_num_5);
        TextView num6 = dialogView.findViewById(R.id.key_num_6);
        TextView num7 = dialogView.findViewById(R.id.key_num_7);
        TextView num8 = dialogView.findViewById(R.id.key_num_8);
        TextView num9 = dialogView.findViewById(R.id.key_num_9);
        TextView numPoint = dialogView.findViewById(R.id.key_num_point);
        TextView numOption = dialogView.findViewById(R.id.key_option);
        ImageButton del = dialogView.findViewById(R.id.key_del);
        if (isDecimal) {
            numPoint.setText(decimalSeparator);
        } else numPoint.setVisibility(View.INVISIBLE);
        num0.setOnClickListener(this);
        num1.setOnClickListener(this);
        num2.setOnClickListener(this);
        num3.setOnClickListener(this);
        num4.setOnClickListener(this);
        num5.setOnClickListener(this);
        num6.setOnClickListener(this);
        num7.setOnClickListener(this);
        num8.setOnClickListener(this);
        num9.setOnClickListener(this);
        numPoint.setOnClickListener(this);
        del.setOnClickListener(this);
        numOption.setVisibility(View.INVISIBLE);
        editTextId = this.editText.getId();
        editText.setOnTouchListener(this::onTouch);
        alertBuilder.setView(dialogView);
        alertBuilder.setPositiveButton("OK", (dialog, which) -> {
            yesButton();
        });
        alertBuilder.setNegativeButton("CANCEL", (dialog, which) -> {
            // do nothing
        });
        alertDialog = alertBuilder.create();
        result.setText(resValueTmp.toString());
    }

    private void yesButton() {
        try {
            Log.d(TAG, "Listener is available : " + listener);
            if (ValueUtils.nonNull(listener)) listener.onSubmitValue();
            if (ValueUtils.nonNull(numberFormat)) {
                if (isDecimal) {
                    editText.setText(result.getText());
                } else {
                    editText.setText(numberFormat.format(numberFormat.parse(resValueTmp.toString()).intValue()));
                }
            } else {
                editText.setText(resValueTmp);
            }
            if (ValueUtils.nonNull(listener)) {
                if (isDecimal) {
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
        isScroll = scroll;
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
        titleContent.setVisibility(View.VISIBLE);
        titleLeft.setText(left);
        titleRight.setText(right);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        if (v.getId() == editTextId) {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                resValueTmp.delete(0, resValueTmp.length());
                resValueTmp.append(editText.getText().toString());
                result.setText(resValueTmp.toString());
                if (ValueUtils.nonNull(context)) {
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (ValueUtils.nonNull(imm)) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
                Log.d(TAG, "Action touch event : " + motionEvent.getAction());
                alertDialog.show();
            }
        } else {
            try {
                int i = v.getId();
                if (i == R.id.key_num_0) {
                    inputValue("0");

                } else if (i == R.id.key_num_1) {
                    inputValue("1");

                } else if (i == R.id.key_num_2) {
                    inputValue("2");

                } else if (i == R.id.key_num_3) {
                    inputValue("3");

                } else if (i == R.id.key_num_4) {
                    inputValue("4");

                } else if (i == R.id.key_num_5) {
                    inputValue("5");

                } else if (i == R.id.key_num_6) {
                    inputValue("6");

                } else if (i == R.id.key_num_7) {
                    inputValue("7");

                } else if (i == R.id.key_num_8) {
                    inputValue("8");

                } else if (i == R.id.key_num_9) {
                    inputValue("9");

                } else if (i == R.id.key_num_point) {
                    if (isDecimal) {
                        inputValue(decimalSeparator);
                    }
                } else if (i == R.id.key_del) {
                    delete();
                }
            } catch (ParseException e) {
                Log.e(TAG, ExceptionUtils.message(e));
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        Log.i(TAG, "ON CLICK ID  :" + v.getId());
        if (isScroll) {
            Log.d(TAG, "root is scrolling");
            return;
        }
        if (v.getId() == editTextId) {
            resValueTmp.delete(0, resValueTmp.length());
            resValueTmp.append(editText.getText().toString());
            result.setText(resValueTmp.toString());
            if (ValueUtils.nonNull(context)) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (ValueUtils.nonNull(imm)) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
            alertDialog.show();
        } else {
            try {
                int i = v.getId();
                if (i == R.id.key_num_0) {
                    inputValue("0");

                } else if (i == R.id.key_num_1) {
                    inputValue("1");

                } else if (i == R.id.key_num_2) {
                    inputValue("2");

                } else if (i == R.id.key_num_3) {
                    inputValue("3");

                } else if (i == R.id.key_num_4) {
                    inputValue("4");

                } else if (i == R.id.key_num_5) {
                    inputValue("5");

                } else if (i == R.id.key_num_6) {
                    inputValue("6");

                } else if (i == R.id.key_num_7) {
                    inputValue("7");

                } else if (i == R.id.key_num_8) {
                    inputValue("8");

                } else if (i == R.id.key_num_9) {
                    inputValue("9");

                } else if (i == R.id.key_num_point) {
                    if (isDecimal) {
                        inputValue(decimalSeparator);
                    }
                } else if (i == R.id.key_del) {
                    delete();
                }
            } catch (ParseException e) {
                Log.e(TAG, ExceptionUtils.message(e));
            }
        }
    }

    private void inputValue(String valueChar) throws ParseException {
        if (isDecimal) {
            inputDouble(valueChar);
        } else inputInteger(valueChar);
    }

    // integer only
    private void inputInteger(String valueChar) throws ParseException {
        BigInteger value = getAvailableIntValue();
        StringBuilder sbBfPoint = new StringBuilder(value.toString());
        sbBfPoint.append(valueChar);
        Log.i(TAG, "integer value : " + value + " | " + sbBfPoint);
        resValueTmp = new StringBuilder(sbBfPoint);
        printValueToUI(value);
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

    private void delete() throws ParseException {
        if (isDecimal) {
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
            BigInteger value = getAvailableIntValue();
            StringBuilder sbBfPoint = new StringBuilder(value.toString());
            int length = sbBfPoint.length();
            for (int i = length; i > 0; i--) {
                sbBfPoint.delete(i - 1, i);
                break;
            }
            length = sbBfPoint.length();
            if (length == 0) {
                sbBfPoint.append("0");
            }
            resValueTmp = new StringBuilder(sbBfPoint);
            printValueToUI(value);
        }
    }

    private BigInteger getAvailableIntValue() throws ParseException {
        long value = 0;
        if (ValueUtils.nonNull(numberFormat)) {
            resValueTmp = new StringBuilder(result.getText().toString());
            value = numberFormat.parse(resValueTmp.toString()).intValue();
        } else {
            if (!resValueTmp.toString().trim().isEmpty()) {
                value = Integer.parseInt(resValueTmp.toString().trim());
            }
        }
        BigInteger bd = BigInteger.valueOf(value);
        Log.i(TAG, "get value : " + bd);
        return bd;
    }

    private BigDecimal getAvailableValue() {
        double value = 0.00;
        if (ValueUtils.nonNull(numberFormat)) {
            String numberString = result.getText().toString();
            resValueTmp = new StringBuilder(numberString);
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

    private void printValueToUI(BigDecimal value) {
        if (Double.parseDouble(resValueTmp.toString()) <= maxDbl) {
            if (ValueUtils.nonNull(numberFormat)) {
                result.setText(currencyFormat(Double.parseDouble(resValueTmp.toString())));
            } else {
                result.setText(String.valueOf(Double.valueOf(resValueTmp.toString())));
            }
            resValueTmp = new StringBuilder(result.getText().toString());
        } else {
            resValueTmp = new StringBuilder(String.valueOf(value));
        }
    }

    private void printValueToUI(BigInteger value) {
        Log.i(TAG, "PRINT : " + resValueTmp);
        if (Long.parseLong(resValueTmp.toString()) <= maxInt) {
            if (ValueUtils.nonNull(numberFormat)) {
                result.setText(numberFormat.format(Integer.valueOf(resValueTmp.toString())));
            } else {
                result.setText(String.valueOf(Integer.valueOf(resValueTmp.toString())));
            }
            resValueTmp = new StringBuilder(result.getText().toString());
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
