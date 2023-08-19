package com.onevour.core.utilities.input;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zuliadin on 08/10/2016.
 * Updated by zuliadin on 30/01/2021.
 */
@SuppressLint("ClickableViewAccessibility")
public class NumberInput implements View.OnTouchListener, NumberInputView.AlertListener {

    private static final String TAG = NumberInput.class.getSimpleName();

    private final Handler handler = new Handler(Looper.getMainLooper());

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

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


    public void setup(@NonNull EditText editText, Listener listener, NumberFormat numberFormat, double min, double max) {
        this.context = editText.getRootView().getContext();
        this.listener = listener;
        this.numberFormat = numberFormat;
        this.editText = editText;
        this.editText.setTextIsSelectable(true);
        this.editText.setCursorVisible(false);
        this.editText.setFocusable(false);
        this.editText.setOnTouchListener(this);
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


    public void disableTouch() {
        editText.setOnTouchListener(null);
    }

    public void enableTouch() {
        editText.setOnTouchListener(this);
    }

    public void show() {
        editText.setOnTouchListener(this);
        editText.dispatchTouchEvent(triggerTouch());
    }

    public void setTitle(String left) {
        if (ValueOf.isEmpty(left)) return;
        alert.setTitle(left, null);
    }

    /*
     * show UI on touch
     *
     * */
    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (ValueOf.nonNull(imm)) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        if (v.getId() == editText.getId() && motionEvent.getAction() == MotionEvent.ACTION_UP) {
            Log.d(TAG, "Action touch event : ".concat(String.valueOf(motionEvent.getAction())));
            executor.execute(() -> {
                try {
                    adapter.setValue(editText.getText().toString());
                    adapter.validateInit();
                    handler.post(() -> alert.show(adapter.getValueString(), adapter.isAfterPoint()));
                } catch (ParseException e) {
                    handler.post(() -> alert.error(e.getMessage()));
                }
            });
        }
        return false;
    }


    @Override
    public void inputValue(String valueChar) {
        executor.execute(() -> {
            try {
                adapter.append(valueChar);
                handler.post(() -> alert.setResult(adapter.getValueString(), adapter.isAfterPoint()));
            } catch (ParseException e) {
                handler.post(() -> alert.error(e.getMessage()));
            }
        });

    }

    @Override
    public void delete() {
        executor.execute(() -> {
            adapter.delete();
            handler.post(() -> alert.setResult(adapter.getValueString(), adapter.isAfterPoint()));
        });
    }

    @Override
    public void submit() {
        editText.setText(adapter.getValueString());
        if (ValueOf.isNull(listener)) return;
        listener.onValue(editText.getId(), isDecimal(), adapter.getValueInteger(), adapter.getValueDouble());
        listener.onSubmitValue();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public void showMaxValue() {
        alert.showMaxValue();
    }

    private MotionEvent triggerTouch() {
        // Obtain MotionEvent object
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis() + 100;
        float x = 0.0f;
        float y = 0.0f;
        int metaState = 0;
        return MotionEvent.obtain(
                downTime,
                eventTime,
                MotionEvent.ACTION_UP,
                x,
                y,
                metaState
        );
    }


    public interface Listener {

        void onSubmitValue();

        void onValue(@IdRes int id, boolean isDecimal, int intValue, double doubleValue);

    }

}