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
    EditText txtIntEntry;
    EditText txtIntExit;
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
        txtIntEntry = findViewById(R.id.txtIntervalEntry);
        txtIntExit = findViewById(R.id.txtIntervalExit);
        txtExit = findViewById(R.id.txtExit);
        fileName = "records.json";

        SimpleMaskFormatter smfd = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher mtwd = new MaskTextWatcher(txtDate, smfd);
        txtDate.addTextChangedListener(mtwd);

        changeFocus();

        if (getDate() != null) {
            txtDate.setText(getDate());
        } else {
            txtDate.setText(TimeUtils.getDate());
        }

        loadForm();

        SimpleMaskFormatter smf = new SimpleMaskFormatter("NN:NN");
        MaskTextWatcher mtw;

        mtw = new MaskTextWatcher(txtEntry, smf);
        txtEntry.addTextChangedListener(mtw);

        mtw = new MaskTextWatcher(txtIntEntry, smf);
        txtIntEntry.addTextChangedListener(mtw);

        mtw = new MaskTextWatcher(txtIntExit, smf);
        txtIntExit.addTextChangedListener(mtw);

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

        if (checkInputs() == 1) {
            Toast.makeText(this, "Formato de horas incorreto.", Toast.LENGTH_SHORT).show();
            return;
        }

        Gson gson = new Gson();

        Register r = new Register(
                txtDate.getText().toString(),
                txtEntry.getText().toString(),
                txtIntEntry.getText().toString(),
                txtIntExit.getText().toString(),
                txtExit.getText().toString()
        );

        String date = r.getDate();

        if (txtEntry.length() > 1 && txtIntEntry.length() > 1 && txtIntExit.length() > 1 && txtExit.length() > 1) {
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

        if (records != null) {
            for (Register reg : records.getRecords()) {
                if (reg.getDate().equals(date)) {
                    txtEntry.setText(reg.getEntry());
                    txtIntEntry.setText(reg.getIntervalEntry());
                    txtIntExit.setText(reg.getIntervalExit());
                    txtExit.setText(reg.getExit());
                }
            }
        }
    }

    private int checkInputs() {
        int r = 0;
        String str1 = txtEntry.getText().toString();
        String[] arr1 = str1.split(":");
        String str2 = txtIntEntry.getText().toString();
        String[] arr2 = str2.split(":");
        String str3 = txtIntExit.getText().toString();
        String[] arr3 = str3.split(":");
        String str4 = txtExit.getText().toString();
        String[] arr4 = str4.split(":");

        if (lengthHour(txtEntry.length()) || lengthHour(txtIntEntry.length()) || lengthHour(txtIntExit.length()) ||
                lengthHour(txtExit.length())) {
            r = 1;
        } else {
            if (txtEntry.length() == 5) {
                if (rangeHour(Integer.parseInt(arr1[0]))) {
                    if (rangeMin(Integer.parseInt(arr1[1]))) {
                        r = 0;
                    } else {
                        r = 1;
                    }
                } else {
                    r = 1;
                }
            }
            if (txtIntEntry.length() == 5) {
                if (rangeHour(Integer.parseInt(arr2[0]))) {
                    if (rangeMin(Integer.parseInt(arr2[1]))) {
                        r = 0;
                    } else {
                        r = 1;
                    }
                } else {
                    r = 1;
                }
            }
            if (txtIntExit.length() == 5) {
                if (rangeHour(Integer.parseInt(arr3[0]))) {
                    if (rangeMin(Integer.parseInt(arr3[1]))) {
                        r = 0;
                    } else {
                        r = 1;
                    }
                } else {
                    r = 1;
                }
            }
            if (txtExit.length() == 5) {
                if (rangeHour(Integer.parseInt(arr4[0]))) {
                    if (rangeMin(Integer.parseInt(arr4[1]))) {
                        r = 0;
                    } else {
                        r = 1;
                    }
                } else {
                    r = 1;
                }
            }
        }

        return r;
    }

    private static boolean lengthHour(int n) {
        return Math.max(1, n) == Math.min(n, 4);
    }

    private static boolean rangeHour(int n) {
        boolean r = Math.max(0, n) == Math.min(n, 23);
        return r;
    }

    private static boolean rangeMin(int n) {
        boolean r = Math.max(0, n) == Math.min(n, 59);
        return r;
    }

    private String getDate() {
        Intent i = getIntent();
        String date = i.getStringExtra("date");
        return date;
    }

    private void clearFields() {
        txtEntry.setText("");
        txtIntEntry.setText("");
        txtIntExit.setText("");
        txtExit.setText("");
    }

    private void setFocus() {
        if (txtEntry.length() > 1 && txtIntEntry.length() > 1 && txtIntExit.length() > 1) {
            txtExit.requestFocus();
        } else if (txtEntry.length() > 1 && txtIntEntry.length() > 1) {
            txtIntExit.requestFocus();
        } else if (txtEntry.length() > 1) {
            txtIntEntry.requestFocus();
        } else {
            txtEntry.requestFocus();
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private void changeFocus() {
        txtEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (txtEntry.length() > 4) {
                    txtIntEntry.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        txtIntEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (txtIntEntry.length() > 4) {
                    txtIntExit.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        txtIntExit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (txtIntExit.length() > 4) {
                    txtExit.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        txtExit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (txtExit.length() > 4) {
                    btnSave.setFocusable(true);
                    btnSave.setFocusableInTouchMode(true);
                    btnSave.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
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