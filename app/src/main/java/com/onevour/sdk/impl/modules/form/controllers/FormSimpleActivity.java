package com.onevour.sdk.impl.modules.form.controllers;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.onevour.core.utilities.format.NFormat;
import com.onevour.core.utilities.input.NumberInput;
import com.onevour.sdk.impl.databinding.ActivityFormSimpleBinding;


//import butterknife.BindView;
//import butterknife.ButterKnife;

public class FormSimpleActivity extends AppCompatActivity {

//    @BindView(R.id.input_number)
//    EditText inputNumber;
//
//    @BindView(R.id.input_decimal)
//    EditText inputDecimal;

    NumberInput number = new NumberInput();

    NumberInput decimal = new NumberInput();

    ActivityFormSimpleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFormSimpleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // ButterKnife.bind(this);
        binding.inputNumber.setText(String.valueOf(Integer.MAX_VALUE));
        binding.inputDecimal.setText(NFormat.currencyFormat(0.00));
        number.setup(binding.inputNumber, 0, Integer.MAX_VALUE);
        number.enableMaxValue();
        decimal.setup(binding.inputDecimal, NFormat.currency(), 0, 100000000);
        decimal.enableMaxValue();
    }
}
