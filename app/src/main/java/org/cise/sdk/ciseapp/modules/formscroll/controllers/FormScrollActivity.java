package org.cise.sdk.ciseapp.modules.formscroll.controllers;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.cise.core.utilities.input.NumberInput;
import org.cise.core.utilities.ui.scroll.ListenScrollChangesHelper;
import org.cise.core.utilities.ui.scroll.OnScrollChangeListenerCompat;
import org.cise.sdk.ciseapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FormScrollActivity extends AppCompatActivity {

    private static final String TAG = "F-SCROLL";

    @BindView(R.id.input_number_1)
    EditText inputNumber1;
    @BindView(R.id.input_number_2)
    EditText inputNumber2;
    @BindView(R.id.input_number_3)
    EditText inputNumber3;
    @BindView(R.id.input_number_4)
    EditText inputNumber4;
    @BindView(R.id.input_number_5)
    EditText inputNumber5;
    @BindView(R.id.input_number_6)
    EditText inputNumber6;
    @BindView(R.id.input_number_7)
    EditText inputNumber7;
    @BindView(R.id.input_number_8)
    EditText inputNumber8;
    @BindView(R.id.input_number_9)
    EditText inputNumber9;
    @BindView(R.id.input_number_10)
    EditText inputNumber10;
    @BindView(R.id.input_number_11)
    EditText inputNumber11;
    @BindView(R.id.input_number_12)
    EditText inputNumber12;
    @BindView(R.id.input_number_13)
    EditText inputNumber13;
    @BindView(R.id.input_number_14)
    EditText inputNumber14;
    @BindView(R.id.input_number_15)
    EditText inputNumber15;

    @BindView(R.id.scroll_view)
    ScrollView scrollView;

    private long lastScrollUpdate = -1;

    private boolean isScroll = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_scroll);
        ButterKnife.bind(this);
        List<EditText> editTextList = new ArrayList<>();
        editTextList.add(inputNumber1);
        editTextList.add(inputNumber2);
        editTextList.add(inputNumber3);
        editTextList.add(inputNumber4);
        editTextList.add(inputNumber5);
        editTextList.add(inputNumber6);
        editTextList.add(inputNumber7);
        editTextList.add(inputNumber8);
        editTextList.add(inputNumber9);
        editTextList.add(inputNumber10);
        editTextList.add(inputNumber11);
        editTextList.add(inputNumber12);
        editTextList.add(inputNumber13);
        editTextList.add(inputNumber14);
        editTextList.add(inputNumber15);
        for (EditText e : editTextList) {
            e.setText(String.valueOf(0));
            NumberInput numberInput = new NumberInput(e);
//            numberInput.setScrollFlag(isScroll);
        }
//        ListenScrollChangesHelper listener = new ListenScrollChangesHelper();
//        listener.addViewToListen(scrollView, (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
//            Log.d(TAG, scrollX + "|" + oldScrollX + "|" + scrollY + "|" + oldScrollY);
//            if (lastScrollUpdate == -1) {
//                scrollView.postDelayed(new ScrollStateHandler(), 100);
//            }
//            lastScrollUpdate = System.currentTimeMillis();
//        });

    }

    private class ScrollStateHandler implements Runnable {

        @Override
        public void run() {
            long currentTime = System.currentTimeMillis();
            if ((currentTime - lastScrollUpdate) > 100) {
                lastScrollUpdate = -1;
                isScroll = true;
                Log.d(TAG, "scroll stop");
            } else {
                scrollView.postDelayed(this, 100);
            }
        }
    }
}
