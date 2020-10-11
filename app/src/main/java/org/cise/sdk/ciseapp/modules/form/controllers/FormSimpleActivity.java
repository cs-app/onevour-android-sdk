package org.cise.sdk.ciseapp.modules.form.controllers;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.cise.core.utilities.format.NFormat;
import org.cise.core.utilities.input.NumberInput;
import org.cise.sdk.ciseapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FormSimpleActivity extends AppCompatActivity {

    @BindView(R.id.input_number)
    EditText inputNumber;

    @BindView(R.id.input_decimal)
    EditText inputDecimal;

    NumberInput number = new NumberInput();

    NumberInput decimal = new NumberInput();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_simple);
        ButterKnife.bind(this);
        inputNumber.setText(String.valueOf(0));
        inputDecimal.setText(NFormat.currencyFormat(0.00));
        number.setup(inputNumber);
        decimal.setup(inputDecimal, true, NFormat.currency(), 0, 10000000);
    }
}
