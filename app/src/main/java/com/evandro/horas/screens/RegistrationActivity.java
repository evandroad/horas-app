package com.evandro.horas.screens;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.evandro.horas.R;
import com.evandro.horas.classes.RegistrationProc;
import com.evandro.horas.util.TimeUtils;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.evandro.horas.classes.Records;
import com.evandro.horas.classes.Register;

import java.util.Calendar;

public class RegistrationActivity extends AppCompatActivity {

    public static final int MSG_RENDER = 0;
    public static final int MSG_SUCCESS = 1;
    public static final int MSG_FORMAT_INCORRECT = 2;

    Button btnSave, btnMore, btnLess;
    EditText txtDate, txtEntry, txtIntEntry, txtIntExit, txtExit;
    private Handler handler;
    private RegistrationProc registrationProc;

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

        btnSave.setOnClickListener((v) -> {
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    updateUI(msg);
                }
            };
            registrationProc = new RegistrationProc(handler);
            registrationProc.start();
            registrationProc.setDate(txtDate.getText().toString());
            registrationProc.setEntry(txtEntry.getText().toString());
            registrationProc.setIntEntry(txtIntEntry.getText().toString());
            registrationProc.setIntExit(txtIntExit.getText().toString());
            registrationProc.setExit(txtExit.getText().toString());
            registrationProc.setProcState(RegistrationProc.PROC_STATE_SAVE);
        });

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

    private void loadForm() {
        String date = txtDate.getText().toString();

        if (Records.getInstance() != null) {
            for (Register reg : Records.getInstance().getRecords()) {
                if (reg.getDate().equals(date)) {
                    txtEntry.setText(reg.getEntry());
                    txtIntEntry.setText(reg.getIntervalEntry());
                    txtIntExit.setText(reg.getIntervalExit());
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
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (txtEntry.length() > 4) {
                    txtIntEntry.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        txtIntEntry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (txtIntEntry.length() > 4) {
                    txtIntExit.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
        txtIntExit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (txtIntExit.length() > 4) {
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
                if (txtExit.length() > 4) {
                    btnSave.setFocusable(true);
                    btnSave.setFocusableInTouchMode(true);
                    btnSave.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void updateUI(Message msg) {
        if (msg.what == MSG_RENDER) {
            clearFields();
            String date = TimeUtils.moreDay(txtDate.getText().toString());
            if (TimeUtils.getDayWeek(date) == Calendar.SUNDAY || TimeUtils.getDayWeek(date) == Calendar.SATURDAY)
                date = TimeUtils.moreTwoDay(date);
            txtDate.setText(date);
            loadForm();
            txtEntry.requestFocus();
        } else if (msg.what == MSG_SUCCESS) {
            Toast.makeText(this, "Dados cadastrados com sucesso.", Toast.LENGTH_SHORT).show();
        } else if (msg.what == MSG_FORMAT_INCORRECT) {
            Toast.makeText(this, "Formato de horas incorreto.", Toast.LENGTH_SHORT).show();
        }
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