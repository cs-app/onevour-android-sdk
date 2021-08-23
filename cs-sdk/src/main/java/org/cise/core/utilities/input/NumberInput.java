package org.cise.core.utilities.input;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;

import org.cise.core.utilities.commons.ValueUtils;

import java.text.NumberFormat;
import java.text.ParseException;

/**
 * Created by zuliadin on 08/10/2016.
 * Updated by zuliadin on 30/01/2021.
 */
public class NumberInput implements View.OnTouchListener, NumberInputGUI.AlertListener {

    private static final String TAG = "NID";

    private Context context;

    private EditText editText;

    private NumberFormat numberFormat;

    private Listener listener;

    private NumberInputGUI alert = new NumberInputGUI();

    private NumberInputAdapter adapter;

    public NumberInput() {

    }

    public NumberInput(final EditText editText) {
        setup(editText);
    }

    public NumberInput(final EditText editText, final NumberFormat numberFormat, double min, double max) {
        setup(editText, numberFormat, min, max);
    }

    public void setup(final EditText editText) {
        setup(editText, null, 0, Integer.MAX_VALUE);
    }

    public void setup(final EditText editText, double max) {
        setup(editText, null, 0, max);
    }

    public void setup(final EditText editText, double min, double max) {
        setup(editText, null, min, max);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setup(@NonNull EditText editText, NumberFormat numberFormat, double min, double max) {
        this.context = editText.getRootView().getContext();
        editText.setTextIsSelectable(true);
        editText.setCursorVisible(false);
        editText.setFocusable(false);
        editText.setOnTouchListener(this);
        this.editText = editText;
        this.numberFormat = numberFormat;
        alert.init(context, numberFormat, min, max, this);
        if (isDecimal()) {
            adapter = new InputDouble(numberFormat, min, max);
        } else {
            adapter = new InputInteger((int) min, (int) max);
        }
    }

    private boolean isDecimal() {
        return null != numberFormat;
    }

    public void setScrollFlag(boolean scroll) {
//        isScroll = scroll;
    }

    @SuppressLint("ClickableViewAccessibility")
    public void disableTouch() {
        editText.setOnTouchListener(null);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void enableTouch() {
        editText.setOnTouchListener(this);
    }

    public void setTitle(String left, String right) {
        if (ValueUtils.isEmpty(left, right)) return;
        alert.setTitle(left, right);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (ValueUtils.nonNull(imm)) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        if (v.getId() == editText.getId() && motionEvent.getAction() == MotionEvent.ACTION_UP) {
            Log.d(TAG, "Action touch event : ".concat(String.valueOf(motionEvent.getAction())));
            try {
                adapter.setValue(editText.getText().toString());
                alert.show(adapter.getValueString());
            } catch (ParseException e) {
                alert.error(e.getMessage());
            }
        }
        return false;
    }

    @Override
    public void inputValue(String valueChar) {
        try {
            adapter.append(valueChar);
            alert.setResult(adapter.getValueString());
        } catch (ParseException e) {
            alert.error(e.getMessage());
        }
    }

    @Override
    public void delete() {
        adapter.delete();
        alert.setResult(adapter.getValueString());
    }

    @Override
    public void submit() {
        if (ValueUtils.nonNull(listener)) listener.onSubmitValue();
        editText.setText(adapter.getValueString());
        if (ValueUtils.isNull(listener)) return;
        if (isDecimal()) {
            listener.doubleValue(adapter.getValueDouble());
        } else listener.intValue(adapter.getValueInteger());
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void showMaxValue() {
        adapter.setMaxValue();
        alert.setResult(adapter.getValueString());
    }

    public void enableMaxValue() {
        alert.showMaxValue();
    }

    public interface Listener {

        void onSubmitValue();

        void doubleValue(double v);

        void intValue(int i);

    }
}
