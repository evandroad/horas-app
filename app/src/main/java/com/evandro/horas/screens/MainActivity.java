package com.evandro.horas.screens;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.evandro.horas.R;
import com.evandro.horas.classes.TimeUtils;
import com.google.gson.Gson;

import com.evandro.horas.classes.JsonUtils;
import com.evandro.horas.classes.Records;
import com.evandro.horas.classes.Register;
import com.evandro.horas.classes.RegisterAdapter;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private TextView tvMainDate;
    private TextView tvOvertime;
    private TextView tvHourLess;
    private TextView tvBalanceMonth;
    private TextView tvTotalMonth;
    private TextView tvMonthYear;
    private TextView tvDayWeek;
    private Records records = new Records();
    RegisterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAdd = findViewById(R.id.btnAdd);
        Button btnMoreDay = findViewById(R.id.btnMoreDay);
        Button btnLessDay = findViewById(R.id.btnLessDay);
        Button btnMoreMonth = findViewById(R.id.btnMoreMonth);
        Button btnLessMonth = findViewById(R.id.btnLessMonth);
        listView = findViewById(R.id.list_records);
        tvMainDate = findViewById(R.id.tvMainDate);
        tvOvertime = findViewById(R.id.tvOvertime);
        tvHourLess = findViewById(R.id.tvHourLess);
        tvTotalMonth = findViewById(R.id.tvTotalMonth);
        tvBalanceMonth = findViewById(R.id.tvBalanceMonth);
        tvMonthYear = findViewById(R.id.tvMonthYear);
        tvDayWeek = findViewById(R.id.tvDayWeek);

        if(getDate() != null) {
            tvMainDate.setText(getDate());
        } else {
            tvMainDate.setText(TimeUtils.getDate());
        }
        tvMonthYear.setText(TimeUtils.getMonthYearString(tvMainDate.getText().toString()));
        tvDayWeek.setText(TimeUtils.getDayWeekString(tvMainDate.getText().toString()));

        JsonUtils.createNewFile(this, TimeUtils.getDate());

        fillTable();

        tvMainDate.setOnClickListener(v -> {
            tvMainDate.setText(TimeUtils.getDate());
            tvMonthYear.setText(TimeUtils.getMonthYearString(tvMainDate.getText().toString()));
            tvDayWeek.setText(TimeUtils.getDayWeekString(tvMainDate.getText().toString()));
            fillTable();
        });

        btnAdd.setOnClickListener((v) -> {
            Intent it = new Intent(MainActivity.this, RegistrationActivity.class);
            it.putExtra("date", tvMainDate.getText().toString());
            startActivity(it);
            finish();
        });

        btnMoreDay.setOnClickListener(v -> {
            String[] d = TimeUtils.moreDayWeek(tvMainDate.getText().toString()).split("-");
            tvDayWeek.setText(d[0]);
            tvMonthYear.setText(d[1]);
            tvMainDate.setText(d[2]);
            fillTable();
        });

        btnLessDay.setOnClickListener(v -> {
            String[] d = TimeUtils.lessDayWeek(tvMainDate.getText().toString()).split("-");
            tvDayWeek.setText(d[0]);
            tvMonthYear.setText(d[1]);
            tvMainDate.setText(d[2]);
            fillTable();
        });

        btnMoreMonth.setOnClickListener(v -> {
            String[] d = TimeUtils.moreMonth(tvMainDate.getText().toString()).split("-");
            tvMonthYear.setText(d[0]);
            tvMainDate.setText(d[1]);
            fillTable();
        });

        btnLessMonth.setOnClickListener(v -> {
            String[] d = TimeUtils.lessMonth(tvMainDate.getText().toString()).split("-");
            tvMonthYear.setText(d[0]);
            tvMainDate.setText(d[1]);
            fillTable();
        });

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getItemId() == 1 ) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Atenção")
                    .setMessage("Realmente deseja excluir este registro?")
                    .setNegativeButton("NÃO", null)
                    .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            delete(item);
                        }
                    }).create();
            dialog.show();

        } else {
            update(item);
        }
        return true;
    }

    private void fillTable() {
        Gson gson = new Gson();
        String date = tvMainDate.getText().toString();
        String fileContent = JsonUtils.getJsonString(this, date);
        records = gson.fromJson(fileContent, Records.class);


        for (int i = 0; i < records.getRecords().size(); i++) {
            int comparison = TimeUtils.compareTo(records.getRecords().get(i).getDate(), date);
            if(comparison == 1) {
                records.getRecords().remove(i);
                i--;
            }
        }

        for ( Register r : records.getRecords() ) {
            r.setDate(r.getDate().substring(0, 5));
        }

        listView.invalidateViews();
        adapter = new RegisterAdapter(this, records.getRecords());
        listView.setAdapter(adapter);
        listView.setOnCreateContextMenuListener((contextMenu, view, contextMenuInfo) -> {
            contextMenu.add(Menu.NONE, 1, Menu.NONE, "Deletar");
            contextMenu.add(Menu.NONE, 2, Menu.NONE, "Atualizar");
        });

        int b, ot = 0, hl = 0, bm = 0, t = 0;

        for(Register r : records.getRecords()) {
            if(r.getEntry().equals("") || r.getIntervalEntry().equals("") || r.getIntervalExit().equals("")
                    || r.getExit().equals("")) {
            } else {
                t = t + TimeUtils.totalMin(r.getEntry(), r.getIntervalEntry(), r.getIntervalExit(), r.getExit());
                b = TimeUtils.balanceMin(r.getEntry(), r.getIntervalEntry(), r.getIntervalExit(), r.getExit());
                bm = bm + b;
                if(b < 0) hl = hl + b;
                if(b > 0) ot = ot + b;
            }
        }
        tvOvertime.setText(TimeUtils.toHour(ot));
        tvHourLess.setText(TimeUtils.toHour(hl));
        tvBalanceMonth.setText(TimeUtils.toHour(bm));
        tvTotalMonth.setText(TimeUtils.toHour(t));

    }

    private void delete(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int pos = menuInfo.position;
        String date = records.getRecords().get(pos).getDate();
        String year = tvMainDate.getText().toString().substring(5, 10);
        date = date + year;
        records.getRecords().remove(pos);

        for ( Register r : records.getRecords() ) {
            r.setDate(r.getDate() + year);
        }

        adapter.notifyDataSetChanged();

        JsonUtils.createJsonFile(this, records, date);

        fillTable();

        Toast.makeText(this, "Registro deletado com sucesso.", Toast.LENGTH_SHORT).show();
    }

    private void update(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int pos = menuInfo.position;
        String date = records.getRecords().get(pos).getDate();
        String year = tvMainDate.getText().toString().substring(5, 10);
        date = date + year;
        adapter.notifyDataSetChanged();
        Intent it = new Intent(this, RegistrationActivity.class);
        it.putExtra("date", date);
        startActivity(it);
        finish();
    }

    private String getDate() {
        Intent i = getIntent();
        String date = i.getStringExtra("date");
        return date;
    }

}