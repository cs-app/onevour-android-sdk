package com.onevour.core.utilities.input;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.onevour.core.R;
import com.onevour.core.utilities.commons.ValueOf;
import com.onevour.core.utilities.format.NFormat;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;


public class NumberInputGUI implements View.OnClickListener {

    private static final String TAG = NumberInputGUI.class.getSimpleName();

    private AlertListener listener;

    private AlertDialog dialog;

    private String decimalSeparator = ".";

    private LinearLayout titleContent;

    private TextView titleLeft, titleRight, numPoint, result;

    private Context context;

    private NumberFormat numberFormat;

    private double min, max;

    protected void init(Context context, NumberFormat numberFormat, double min, double max, AlertListener listener) {
        this.context = context;
        this.listener = listener;
        this.numberFormat = numberFormat;
        this.min = min;
        this.max = max;
        View view = LayoutInflater.from(context).inflate(R.layout.input_number_dialog, null, false);
        titleContent = view.findViewById(R.id.title_content);
        titleContent.setVisibility(View.GONE);
        titleLeft = view.findViewById(R.id.title_left);
        titleRight = view.findViewById(R.id.title_right);
        result = view.findViewById(R.id.key_result);
        TextView num0 = view.findViewById(R.id.key_num_0);
        TextView num1 = view.findViewById(R.id.key_num_1);
        TextView num2 = view.findViewById(R.id.key_num_2);
        TextView num3 = view.findViewById(R.id.key_num_3);
        TextView num4 = view.findViewById(R.id.key_num_4);
        TextView num5 = view.findViewById(R.id.key_num_5);
        TextView num6 = view.findViewById(R.id.key_num_6);
        TextView num7 = view.findViewById(R.id.key_num_7);
        TextView num8 = view.findViewById(R.id.key_num_8);
        TextView num9 = view.findViewById(R.id.key_num_9);
        numPoint = view.findViewById(R.id.key_num_point);
        TextView numOption = view.findViewById(R.id.key_option);
        ImageView del = view.findViewById(R.id.key_del);
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
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(view.getContext());
        alertBuilder.setView(view);
        alertBuilder.setPositiveButton("OK", (dialog, which) -> {
            if (listener == null) return;
            listener.submit();
        });
        alertBuilder.setNegativeButton("CANCEL", (dialog, which) -> {
            // do nothing
        });
        dialog = alertBuilder.create();
        if (null != numberFormat) {
            DecimalFormatSymbols d = ((DecimalFormat) numberFormat).getDecimalFormatSymbols();
            decimalSeparator = String.valueOf(d.getDecimalSeparator());
            numPoint.setText(decimalSeparator);
        } else numPoint.setVisibility(View.INVISIBLE);
    }

    public void show() {
        if (null == dialog) return;
        dialog.show();
    }

    public void show(String value) {
        if (null == dialog) return;
        dialog.show();
        result.setText(value);
    }

    @Override
    public void onClick(View v) {
        if (null == listener) return;
        try {
            int i = v.getId();
            if (i == R.id.key_num_0) {
                listener.inputValue("0");
            } else if (i == R.id.key_num_1) {
                listener.inputValue("1");
            } else if (i == R.id.key_num_2) {
                listener.inputValue("2");
            } else if (i == R.id.key_num_3) {
                listener.inputValue("3");
            } else if (i == R.id.key_num_4) {
                listener.inputValue("4");
            } else if (i == R.id.key_num_5) {
                listener.inputValue("5");
            } else if (i == R.id.key_num_6) {
                listener.inputValue("6");
            } else if (i == R.id.key_num_7) {
                listener.inputValue("7");
            } else if (i == R.id.key_num_8) {
                listener.inputValue("8");
            } else if (i == R.id.key_num_9) {
                listener.inputValue("9");
            } else if (i == R.id.key_num_point) {
                listener.inputValue(decimalSeparator);
            } else if (i == R.id.key_del) {
                listener.delete();

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setResult(String value, boolean isAfterPoint) {
        numPoint.setTextColor(isAfterPoint ? Color.RED : Color.BLACK);
        result.setText(value);
    }

    public void error(String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setTitle("Opps, something wrong!");
        StringBuilder sb = new StringBuilder();
        sb.append(message);
        if (null != numberFormat) {
            DecimalFormatSymbols d = ((DecimalFormat) numberFormat).getDecimalFormatSymbols();
            sb.append("\ndecimal : ").append(d.getDecimalSeparator());
            sb.append("\ngroup : ").append(d.getGroupingSeparator());
            sb.append("\ndecimal monetary : ").append(d.getMonetaryDecimalSeparator());
            sb.append("\ndecimal currency : ").append(d.getCurrencySymbol());
            sb.append("\nexponent : ").append(d.getExponentSeparator());
        }
        alertBuilder.setMessage(sb.toString());
        dialog.dismiss();
        alertBuilder.create().show();
    }

    public void setTitle(String title) {
        setTitle(title, null);
    }

    public void setTitleRight(String title) {
        setTitle(null, title);
    }

    public void setTitle(String left, String right) {
        titleContent.setVisibility(View.VISIBLE);
        titleLeft.setText(left);
        titleRight.setText(right);
        titleRight.setOnClickListener(v -> listener.showMaxValue());
        if (null == left) titleLeft.setVisibility(View.INVISIBLE);
        if (null == right) titleRight.setVisibility(View.INVISIBLE);

    }

    public void showMaxValue() {
        if (null == numberFormat) {
            setTitle(null, String.valueOf(Double.valueOf(max).intValue()));
        } else setTitle(null, numberFormat.format(max));
    }

    public void updateMinMax(double min, double max) {
        titleContent.setVisibility(View.VISIBLE);
        titleRight.setVisibility(View.VISIBLE);
        this.min = min;
        this.max = max;
        if (ValueOf.isNull(numberFormat)) {
            titleRight.setText(String.valueOf((int) max));
        } else {
            titleRight.setText(numberFormat.format(max));
        }
    }

    public interface AlertListener {

        void inputValue(String value) throws ParseException;

        void showMaxValue();

        void delete() throws ParseException;

        void submit();
    }

}
