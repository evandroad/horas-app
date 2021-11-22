package com.evandro.horas.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.evandro.horas.R;
import com.evandro.horas.classes.TimeUtils;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.evandro.horas.classes.JsonUtils;
import com.evandro.horas.classes.Records;
import com.evandro.horas.classes.Register;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class RegistrationActivity extends AppCompatActivity {

    Button btnSave;
    Button btnMore;
    Button btnLess;
    EditText txtDate;
    EditText txtEntry;
    EditText txtIntervalEntry;
    EditText txtIntervalExit;
    EditText txtExit;
    String fileName = "";
    String fileContent = "";
    Records records = new Records();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        btnSave = findViewById(R.id.btnSave);
        btnMore = findViewById(R.id.btnMore);
        btnLess = findViewById(R.id.btnLess);
        txtDate = findViewById(R.id.txtDate);
        txtEntry = findViewById(R.id.txtEntry);
        txtIntervalEntry = findViewById(R.id.txtIntervalEntry);
        txtIntervalExit = findViewById(R.id.txtIntervalExit);
        txtExit = findViewById(R.id.txtExit);
        fileName = "records.json";

        SimpleMaskFormatter smfd = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher mtwd = new MaskTextWatcher(txtDate, smfd);
        txtDate.addTextChangedListener(mtwd);

        if(getDate() != null) {
            txtDate.setText(getDate());
        } else {
            txtDate.setText(TimeUtils.getDate());
        }

        loadForm();

        SimpleMaskFormatter smf = new SimpleMaskFormatter("NN:NN");
        MaskTextWatcher mtw;

        mtw = new MaskTextWatcher(txtEntry, smf);
        txtEntry.addTextChangedListener(mtw);

        mtw = new MaskTextWatcher(txtIntervalEntry, smf);
        txtIntervalEntry.addTextChangedListener(mtw);

        mtw = new MaskTextWatcher(txtIntervalExit, smf);
        txtIntervalExit.addTextChangedListener(mtw);

        mtw = new MaskTextWatcher(txtExit, smf);
        txtExit.addTextChangedListener(mtw);

        txtEntry.requestFocus();

        btnSave.setOnClickListener((v) -> save());

        btnMore.setOnClickListener((v) -> {
            clearFields();
            String date = TimeUtils.moreDay(txtDate.getText().toString());
            txtDate.setText(date);
            loadForm();
        });

        btnLess.setOnClickListener((v) -> {
            clearFields();
            String date = TimeUtils.lessDay(txtDate.getText().toString());
            txtDate.setText(date);
            loadForm();
        });

    }

    private void save() {

        Gson gson = new Gson();

        Register r = new Register(
                txtDate.getText().toString(),
                txtEntry.getText().toString(),
                txtIntervalEntry.getText().toString(),
                txtIntervalExit.getText().toString(),
                txtExit.getText().toString()
        );

        String date = r.getDate();

        JsonUtils.createNewFile(this, date);

        fileContent = JsonUtils.getJsonString(this, date);
        Log.d("tag", "filecontent: " + fileContent);
        records = gson.fromJson(fileContent, Records.class);

        records.getRecords().removeIf(re -> re.getDate().equals(r.getDate()));

        records.setRecords(r);
        Collections.sort(records.getRecords(), Collections.reverseOrder());

        JsonUtils.createJsonFile(this, records, date);

        txtEntry.requestFocus();

        Toast.makeText(this, "Dados cadastrados com sucesso.", Toast.LENGTH_SHORT).show();

    }

    private void loadForm() {
        String date;
        Gson gson = new Gson();

        date = txtDate.getText().toString();
        fileContent = JsonUtils.getJsonString(this, date);

        records = gson.fromJson(fileContent, Records.class);

        if(records != null) {
            for(Register reg : records.getRecords()) {
                if (reg.getDate().equals(date)) {
                    txtEntry.setText(reg.getEntry());
                    txtIntervalEntry.setText(reg.getIntervalEntry());
                    txtIntervalExit.setText(reg.getIntervalExit());
                    txtExit.setText(reg.getExit());
                }
            }
        }
    }

    private String getDate() {
        Intent i = getIntent();
        String date = i.getStringExtra("date");
        return date;
    }

    private void clearFields() {
        txtEntry.setText("");
        txtIntervalEntry.setText("");
        txtIntervalExit.setText("");
        txtExit.setText("");
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(RegistrationActivity.this, MainActivity.class);
        it.putExtra("date", txtDate.getText().toString());
        startActivity(it);
        finish();
        super.onBackPressed();
    }

}