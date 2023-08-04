package com.onevour.sdk.impl.modules.form.controllers;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.onevour.core.utilities.format.NFormat;
import com.onevour.core.utilities.input.NumberInput;
import com.onevour.sdk.impl.databinding.ActivityFormSimpleBinding;


public class FormSimpleActivity extends AppCompatActivity {

    private final NumberInput number = new NumberInput();

    private final NumberInput decimal = new NumberInput();

    private final NumberInput decimal2 = new NumberInput();

    ActivityFormSimpleBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFormSimpleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.inputNumber.setText(String.valueOf(Integer.MAX_VALUE));
        binding.inputDecimal.setText(NFormat.currencyFormat(5603169.26));
        binding.inputDecimal2.setText(NFormat.currencyFormat(100000.0));
//        binding.inputDecimal.setText(NFormat.currencyFormat(0.00));

        number.setup(binding.inputNumber, 0, Integer.MAX_VALUE);
        decimal.setup(binding.inputDecimal, NFormat.currency(), 0, 5603169.26);
        decimal2.setup(binding.inputDecimal2, NFormat.currency(), 0, Double.MAX_VALUE);
    }
}
