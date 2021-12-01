package com.evandro.horas.screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.evandro.horas.R;
import com.evandro.horas.classes.TimeUtils;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.gson.Gson;
import java.util.Collections;

import com.evandro.horas.classes.JsonUtils;
import com.evandro.horas.classes.Records;
import com.evandro.horas.classes.Register;

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

        changeFocus();

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

        setFocus();

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

        if(checkInputs()) {
            Toast.makeText(this, "Formato de horas incorreto.", Toast.LENGTH_SHORT).show();
            return;
        }

        Gson gson = new Gson();

        Register r = new Register(
                txtDate.getText().toString(),
                txtEntry.getText().toString(),
                txtIntervalEntry.getText().toString(),
                txtIntervalExit.getText().toString(),
                txtExit.getText().toString()
        );

        String date = r.getDate();

        if(txtEntry.length() > 1 && txtIntervalEntry.length() > 1 && txtIntervalExit.length() > 1 && txtExit.length() > 1) {
            clearFields();
            date = TimeUtils.moreDay(txtDate.getText().toString());
            txtDate.setText(date);
            loadForm();
        }

        JsonUtils.createNewFile(this, date);

        fileContent = JsonUtils.getJsonString(this, date);
        records = gson.fromJson(fileContent, Records.class);

        records.getRecords().removeIf(re -> re.getDate().equals(r.getDate()));

        records.setRecords(r);
        Collections.sort(records.getRecords(), Collections.reverseOrder());

        JsonUtils.createJsonFile(this, records, date);

        txtEntry.requestFocus();

        Toast.makeText(this, "Dados cadastrados com sucesso.", Toast.LENGTH_SHORT).show();

    }

    private void loadForm() {
        String date = txtDate.getText().toString();
        fileContent = JsonUtils.getJsonString(this, date);

        records = new Gson().fromJson(fileContent, Records.class);

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

    private boolean checkInputs() {
        boolean r;
        int len1 = txtEntry.length();
        int len2 = txtIntervalEntry.length();
        int len3 = txtIntervalExit.length();
        int len4 = txtExit.length();
        String str1 = txtEntry.getText().toString();
        String[] arr1 = str1.split(":");
        if (
            (Math.max(1, len1) == Math.min(len1, 4)) ||
            (Math.max(1, len2) == Math.min(len2, 4)) ||
            (Math.max(1, len3) == Math.min(len3, 4)) ||
            (Math.max(1, len4) == Math.min(len4, 4)) ||
            !(Math.max(0, Integer.parseInt(arr1[0])) == Math.min(Integer.parseInt(arr1[0]), 24)) ||
            !(Math.max(0, Integer.parseInt(arr1[1])) == Math.min(Integer.parseInt(arr1[1]), 59))
        ) {
            r = true;
        } else {
            r = false;
        }
        return r;
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

    private void setFocus() {
        if (txtEntry.length() > 1 && txtIntervalEntry.length() > 1 && txtIntervalExit.length() > 1) {
            txtExit.requestFocus();
        } else if (txtEntry.length() > 1 && txtIntervalEntry.length() > 1) {
            txtIntervalExit.requestFocus();
        } else if (txtEntry.length() > 1) {
            txtIntervalEntry.requestFocus();
        } else {
            txtEntry.requestFocus();
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private void changeFocus() {
        txtEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(txtEntry.length() > 4) {
                    txtIntervalEntry.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
        txtIntervalEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(txtIntervalEntry.length() > 4) {
                    txtIntervalExit.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
        txtIntervalExit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(txtIntervalExit.length() > 4) {
                    txtExit.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
        txtExit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(txtExit.length() > 4) {
                    btnSave.setFocusable(true);
                    btnSave.setFocusableInTouchMode(true);
                    btnSave.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
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