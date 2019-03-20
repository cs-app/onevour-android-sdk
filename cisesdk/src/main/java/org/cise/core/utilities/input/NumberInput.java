package org.cise.core.utilities.input;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.cise.core.R;

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
public class NumberInput implements View.OnClickListener, View.OnTouchListener {

    private static final String TAG = "NID";
    private Context context;
    private AlertDialog alertDialog;
    private EditText editText;
    private TextView result;
    private StringBuilder resValueTmp;
    private int editTextId = 0;
    private double  maxDbl;
    private int maxInt;
    private boolean isDecimal = false, isPoint = false, isAfterPoint = false;
    private NumberFormat numberFormat;
    private String defaultCommaSymbol = ".";
    private LinearLayout titleContent;
    private TextView titleLeft, titleRight;
    private listener listener;

    public NumberInput(final EditText editText) {
        initialize(editText, false, null, 0, Integer.MAX_VALUE);
    }

    public NumberInput(final EditText editText, final boolean isDecimal, final NumberFormat numberFormat, double min, double max) {
        initialize(editText, isDecimal, numberFormat, min, max);
    }

    private void initialize(final EditText editTextView, final boolean isDecimal, final NumberFormat numberFormat, double min, double max) {
        context = editTextView.getRootView().getContext();
        editText = editTextView;
        editText.setTextIsSelectable(true);
        editText.setCursorVisible(false);
        editText.setFocusable(false);
        if (editText.getText().toString().isEmpty()) editText.setText("0");
        this.isDecimal = isDecimal;
        this.numberFormat = numberFormat;
        if (this.numberFormat != null) {
            if (this.numberFormat instanceof DecimalFormat) {
                DecimalFormatSymbols d = ((DecimalFormat) this.numberFormat).getDecimalFormatSymbols();
                defaultCommaSymbol = String.valueOf(d.getDecimalSeparator());
                Log.i(TAG, defaultCommaSymbol);
            }
        }
        if (isDecimal) {
//            minDbl = min;
            maxDbl = max;
            resValueTmp = new StringBuilder(String.valueOf(min));
        } else {
//            minInt = (int) min;
            maxInt = (int) max;
            resValueTmp = new StringBuilder(String.valueOf(((int) min)));
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
        ImageButton del = dialogView.findViewById(R.id.key_del);
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
        editTextId = this.editText.getId();
        // editText.setOnTouchListener(this);
        alertBuilder.setView(dialogView);
        alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "listener is avaiable : " + listener);
                if (null != numberFormat) {
                    if (isDecimal) {
                        editText.setText(result.getText());
                        if (listener != null) {
                            listener.onValueChange(editText.getText().toString());
                        }
                    } else {
                        try {
                            editText.setText(numberFormat.format(numberFormat.parse(resValueTmp.toString()).intValue()));
                            if (listener != null) {
                                listener.onValueChange(editText.getText().toString());
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    editText.setText(resValueTmp);
                    if (listener != null) {
                        listener.onValueChange(editText.getText().toString());
                    }
                }
            }
        });
        alertBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog = alertBuilder.create();
        result.setText(resValueTmp.toString());
    }

    public void setTitle(String left, String right) {
        titleContent.setVisibility(View.VISIBLE);
        titleLeft.setText(left);
        titleRight.setText(right);
    }

    @Override
    public void onClick(View v) {
        Log.i("NumberInput", "ON CLICK ID  :" + v.getId());
        if (v.getId() == editTextId) {
            resValueTmp.delete(0, resValueTmp.length());
            resValueTmp.append(editText.getText().toString());
            result.setText(resValueTmp.toString());
            if (null != context) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            } else {
                Log.w("NumperInput", "context hide softkeyboard is null");
            }
            /*
            DialogHelper.hideSoftKeyboard(context);
            */
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
                        inputValue(defaultCommaSymbol);
                    }
                } else if (i == R.id.key_del) {
                    delete();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void inputValue(String valueChar) throws ParseException {
        if (isDecimal) {
            if (valueChar.equalsIgnoreCase(defaultCommaSymbol)) {
                isPoint = true;
                isAfterPoint = true;
            } else {
                BigDecimal value = getAvailableValue();
                String partValue[] = String.valueOf(value).split("[.]");
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
                        Log.i("NumberInputValue", sbBfPoint.toString());
                    }
                }
                resValueTmp = new StringBuilder(sbBfPoint);
                printValueToUI(value);
            }
        } else {
            BigInteger value = getAvailableIntValue();
            StringBuilder sbBfPoint = new StringBuilder(value.toString());
            sbBfPoint.append(valueChar);
            Log.i("NumberInputValue", "integer value : " + value + " | " + sbBfPoint);
            resValueTmp = new StringBuilder(sbBfPoint);
            printValueToUI(value);
        }
    }

    private void delete() throws ParseException {
        if (isDecimal) {
            BigDecimal value = getAvailableValue();
            String partValue[] = String.valueOf(value).split("[.]");
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
                if (length > 0) sbBfPoint.delete(length - 1, length);
//                for (int i = length; i > 0; i--) {
//                    sbBfPoint.delete(i - 1, i);
//                    break;
//                }
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
            if (length > 0) sbBfPoint.delete(length - 1, length);
//            for (int i = length; i > 0; i--) {
//                sbBfPoint.delete(i - 1, i);
//                break;
//            }
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
        if (numberFormat != null) {
            resValueTmp = new StringBuilder(result.getText().toString());
            value = numberFormat.parse(resValueTmp.toString()).intValue();
        } else {
            if (!resValueTmp.toString().trim().isEmpty()) {
                value = Integer.valueOf(resValueTmp.toString().trim());
            }
        }
        BigInteger bd = BigInteger.valueOf(value);
        Log.i("NumberInputValue", "get value : " + bd);
        return bd;
    }

    private BigDecimal getAvailableValue() throws ParseException {
        double value = 0.00;
        if (numberFormat != null) {
            resValueTmp = new StringBuilder(result.getText().toString()); //result.getText().toString()
            value = numberFormat.parse(resValueTmp.toString()).doubleValue();
        } else {
            if (!resValueTmp.toString().trim().isEmpty())
                value = Double.valueOf(resValueTmp.toString());
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        Log.i("NumberInputValue", "get value : " + bd);
        return bd;
    }

    private int getAfterComma(BigDecimal a) {
        String[] result = String.valueOf(a).split("[.]");
        if (result.length > 1) {
            return Integer.valueOf(result[1]);
        } else {
            return 0;
        }
    }

    private void printValueToUI(BigDecimal value) {
        if (Double.valueOf(resValueTmp.toString()) <= maxDbl) {
            if (numberFormat != null) {
                result.setText(numberFormat.format(Double.valueOf(resValueTmp.toString())));
            } else {
                result.setText(String.valueOf(Double.valueOf(resValueTmp.toString())));
            }
            resValueTmp = new StringBuilder(result.getText().toString());
        } else {
            resValueTmp = new StringBuilder(String.valueOf(value));
        }
    }

    private void printValueToUI(BigInteger value) {
        Log.i("NumberInput", "PRINT : " + resValueTmp);
        if (Long.valueOf(resValueTmp.toString()) <= maxInt) {
            if (numberFormat != null) {
                result.setText(numberFormat.format(Integer.valueOf(resValueTmp.toString())));
            } else {
                result.setText(String.valueOf(Integer.valueOf(resValueTmp.toString())));
            }
            resValueTmp = new StringBuilder(result.getText().toString());
        } else {
            resValueTmp = new StringBuilder(String.valueOf(value));
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        if (v.getId() == editTextId) {
            resValueTmp.delete(0, resValueTmp.length());
            resValueTmp.append(editText.getText().toString());
            result.setText(resValueTmp.toString());
            if (null != context) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                if(imm!=null)imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            } else {
                Log.w("NumperInput", "context hide softkeyboard is null");
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
                        inputValue(defaultCommaSymbol);
                    }
                } else if (i == R.id.key_del) {
                    delete();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void setListener(NumberInput.listener listener) {
        this.listener = listener;
    }

    public interface listener {

        void onValueChange(String value);
    }
}
