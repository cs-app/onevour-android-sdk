package com.onevour.sdk.impl.modules.form.controllers;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.onevour.core.utilities.json.gson.GsonHelper;
import com.onevour.sdk.impl.databinding.ActivityDeepLinkBinding;

public class DeepLinkActivity extends AppCompatActivity {

    ActivityDeepLinkBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeepLinkBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.result.setText(null);
        binding.result.setMovementMethod(new ScrollingMovementMethod());
        binding.button.setOnClickListener(this::openRKTP);
    }

    private void openRKTP(View view) {
        try {
            binding.result.setText(null);
            Uri uri = Uri.parse("rektp://read?type=0");
            rktpLauncher.launch(new Intent(Intent.ACTION_VIEW, uri));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Aplikasi REKTP belum terpasang di device kamu. Harap install terlebih dahulu.", Toast.LENGTH_LONG).show();
        }
    }

    ActivityResultLauncher<Intent> rktpLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == Activity.RESULT_OK) {
            // There are no request codes
            Intent data = result.getData();
            String ektp = data.getStringExtra("ektp");
            DeeplinkResult deeplinkResult = GsonHelper.gson.fromJson(ektp, DeeplinkResult.class);
            Gson gson = new GsonBuilder()
                    .serializeNulls()
                    .setPrettyPrinting()
                    .create();
            deeplinkResult.setFace(null);
            deeplinkResult.setSignature(null);
            deeplinkResult.setFp(null);
            binding.result.setText(gson.toJson(deeplinkResult));
            Toast.makeText(this, "Callback from intent success", Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(this, "Callback from intent " + result.getResultCode(), Toast.LENGTH_LONG).show();
    });

}