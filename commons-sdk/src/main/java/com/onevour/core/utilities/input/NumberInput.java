package com.onevour.core.utilities.input;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;

import com.onevour.core.utilities.commons.ValueOf;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Objects;

/**
 * Created by zuliadin on 08/10/2016.
 * Updated by zuliadin on 30/01/2021.
 */
public class NumberInput implements View.OnTouchListener, NumberInputView.AlertListener {

    private static final String TAG = NumberInput.class.getSimpleName();

    private final NumberInputView alert = new NumberInputView();

    private Context context;

    private EditText editText;

    private NumberFormat numberFormat;

    private Listener listener;

    private NumberInputAdapter adapter;

    public NumberInput() {

    }

    public NumberInput(final EditText editText) {
        setup(editText);
    }

    public NumberInput(final EditText editText, final NumberFormat numberFormat, double min, double max) {
        setup(editText, numberFormat, min, max);
    }

    public void setup(final Context context) {
        setup(new EditText(context), null, null, 0, Integer.MAX_VALUE);
    }

    public void setup(final Context context, double max) {
        setup(new EditText(context), null, null, 0, max);
    }

    public void setup(final Context context, double min, double max) {
        setup(new EditText(context), null, null, min, max);
    }

    public void setup(final Context context, NumberFormat numberFormat, double min, double max) {
        setup(new EditText(context), null, null, min, max);
    }

    public void setup(final Context context, Listener listener, NumberFormat numberFormat, double min, double max) {
        setup(new EditText(context), listener, numberFormat, min, max);
    }

    public void setup(final EditText editText) {
        setup(editText, null, null, 0, Integer.MAX_VALUE);
    }

    public void setup(final EditText editText, double max) {
        setup(editText, null, null, 0, max);
    }

    public void setup(final EditText editText, double min, double max) {
        setup(editText, null, null, min, max);
    }

    public void setup(@NonNull EditText editText, NumberFormat numberFormat, double min, double max) {
        setup(editText, null, numberFormat, min, max);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setup(@NonNull EditText editText, Listener listener, NumberFormat numberFormat, double min, double max) {
        this.context = editText.getRootView().getContext();
        this.listener = listener;
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

    public void updateMinMax(int min, int max) {
        alert.updateMinMax(min, max);
        adapter.updateMinMax(min, max);
    }

    public void updateMinMax(double min, double max) {
        alert.updateMinMax(min, max);
        adapter.updateMinMax(min, max);
    }

    @Override
    public void submitToMaxValue() {
        try {
            adapter.setValueToMax();
            adapter.validateInit();
            alert.show(adapter.getValueString(), adapter.isAfterPoint());
        } catch (ParseException e) {
            alert.error(e.getMessage());
        }
    }

    private boolean isDecimal() {
        return Objects.nonNull(numberFormat);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void disableTouch() {
        editText.setOnTouchListener(null);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void enableTouch() {
        editText.setOnTouchListener(this);
    }

    public void setTitle(String left) {
        if (ValueOf.isEmpty(left)) return;
        alert.setTitle(left, null);
    }

    /*
     * show UI on touch
     *
     * */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (ValueOf.nonNull(imm))
            imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        if (!(v.getId() == editText.getId() && motionEvent.getAction() == MotionEvent.ACTION_UP)) {
            return false;
        }
        Log.d(TAG, "Action touch event : ".concat(String.valueOf(motionEvent.getAction())));
        try {
            adapter.setValue(editText.getText().toString());
            adapter.validateInit();
            alert.show(adapter.getValueString(), adapter.isAfterPoint());
        } catch (ParseException e) {
            alert.error(e.getMessage());
        }
        return false;
    }


    @Override
    public void inputValue(String valueChar) {
        try {
            adapter.append(valueChar);
            alert.setResult(adapter.getValueString(), adapter.isAfterPoint());
        } catch (ParseException e) {
            alert.error(e.getMessage());
        }
    }

    @Override
    public void delete() {
        adapter.delete();
        alert.setResult(adapter.getValueString(), adapter.isAfterPoint());
    }

    @Override
    public void submit() {
        if (ValueOf.nonNull(listener)) listener.onSubmitValue();
        editText.setText(adapter.getValueString());
        if (ValueOf.isNull(listener)) return;
        listener.onValue(editText.getId(), isDecimal(), adapter.getValueInteger(), adapter.getValueDouble());
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void showMaxValue() {
        alert.showMaxValue();
    }


    public interface Listener {

        void onSubmitValue();

        void onValue(@IdRes int id, boolean isDecimal, int intValue, double doubleValue);

    }
}
