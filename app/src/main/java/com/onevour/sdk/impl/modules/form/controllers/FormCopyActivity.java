package com.onevour.sdk.impl.modules.form.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.onevour.core.utilities.beans.BeanCopy;
import com.onevour.sdk.impl.R;
import com.onevour.sdk.impl.databinding.ActivityFormCopyBinding;
import com.onevour.sdk.impl.repositories.models.Employee;
import com.onevour.sdk.impl.repositories.models.Person;

public class FormCopyActivity extends AppCompatActivity {

    ActivityFormCopyBinding binding;

    Employee employee = new Employee();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFormCopyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnCopy.setOnClickListener(this::copyValue);
        employee.setId("1");
        employee.setName("John Doe");
        binding.source.setText(gson().toJson(employee));
    }

    private void copyValue(View view) {
        try {
            Person person = BeanCopy.value(employee, Person.class);
            binding.target.setText(gson().toJson(person));
        } catch (Exception e){
            binding.target.setText(e.getMessage());
        }

    }

    private Gson gson(){
        return new GsonBuilder()
                .serializeNulls()
                .setPrettyPrinting()
                .create();
    }
}