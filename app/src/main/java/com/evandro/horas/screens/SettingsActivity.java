package com.evandro.horas.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.evandro.horas.R;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

public class SettingsActivity extends AppCompatActivity {

    EditText txtStartDate;
    Button btnSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        txtStartDate = findViewById(R.id.txtStartDate);
        btnSettings = findViewById(R.id.btnSettings);

        SimpleMaskFormatter smfd = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher mtwd = new MaskTextWatcher(txtStartDate, smfd);
        txtStartDate.addTextChangedListener(mtwd);

        btnSettings.setOnClickListener(v -> {

        });

    }
}