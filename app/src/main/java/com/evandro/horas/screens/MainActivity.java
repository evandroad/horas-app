package com.evandro.horas.screens;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import com.evandro.horas.classes.RegisterDAO;
import com.evandro.horas.util.TimeUtils;
import com.evandro.horas.util.FileUtil;
import com.evandro.horas.classes.Records;
import com.evandro.horas.classes.Register;
import com.evandro.horas.classes.RegisterAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final List<Register> records = new ArrayList<>();
    private RegisterAdapter adapter;
    private ListView listView;
    private Button btnAdd, btnMoreDay, btnLessDay, btnMoreMonth, btnLessMonth, btnMenu;
    private TextView tvMainDate, tvOvertime, tvHourLess, tvBalanceMonth, tvTotalMonth, tvMonthYear, tvDayWeek;
    private RegisterDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeComponents();
        dao = new RegisterDAO(this);

        if(getDate() != null) {
            tvMainDate.setText(getDate());
        } else {
            tvMainDate.setText(TimeUtils.getDate());
        }

        tvMonthYear.setText(TimeUtils.getMonthYearString(tvMainDate.getText().toString()));
        tvDayWeek.setText(TimeUtils.getDayWeekString(tvMainDate.getText().toString()));

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

        btnMenu.setOnClickListener(v -> {
            Intent it = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(it);
        });

    }

    private void initializeComponents() {
        btnAdd = findViewById(R.id.btnAdd);
        btnMoreDay = findViewById(R.id.btnMoreDay);
        btnLessDay = findViewById(R.id.btnLessDay);
        btnMoreMonth = findViewById(R.id.btnMoreMonth);
        btnLessMonth = findViewById(R.id.btnLessMonth);
        btnMenu = findViewById(R.id.btnMenu);
        listView = findViewById(R.id.list_records);
        tvMainDate = findViewById(R.id.tvMainDate);
        tvOvertime = findViewById(R.id.tvOvertime);
        tvHourLess = findViewById(R.id.tvHourLess);
        tvTotalMonth = findViewById(R.id.tvTotalMonth);
        tvBalanceMonth = findViewById(R.id.tvBalanceMonth);
        tvMonthYear = findViewById(R.id.tvMonthYear);
        tvDayWeek = findViewById(R.id.tvDayWeek);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getItemId() == 1 ) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Atenção")
                    .setMessage("Realmente deseja excluir este registro?")
                    .setNegativeButton("NÃO", null)
                    .setPositiveButton("SIM", (dialog1, which) -> delete(item)).create();
            dialog.show();
        } else {
            update(item);
        }
        return true;
    }

    private void fillTable() {
        records.clear();
        List<Register> rec = new ArrayList<>();
        rec = dao.getRecords();
//        String date = tvMainDate.getText().toString();

//        for ( Register r : Records.getInstance().getRecords() ) {
//            Register reg = new Register(r.getDate(), r.getEntry(), r.getIntervalEntry(), r.getIntervalExit(), r.getExit());
//            int comparison = TimeUtils.compareTo(r.getDate(), date);
//            if (comparison != TimeUtils.MAJOR) {
//                records.add(r);
//                reg.setDate(reg.getDate().substring(0, 5));
//                rec.add(reg);
//            }
//        }

        listView.invalidateViews();
        adapter = new RegisterAdapter(this, rec);
        listView.setAdapter(adapter);
        listView.setOnCreateContextMenuListener((contextMenu, view, contextMenuInfo) -> {
            contextMenu.add(Menu.NONE, 1, Menu.NONE, "Deletar");
            contextMenu.add(Menu.NONE, 2, Menu.NONE, "Atualizar");
        });

        int b, ot = 0, hl = 0, bm = 0, t = 0;
        for(Register r : records) {
            t = t + TimeUtils.totalMin(r.getEntry(), r.getIntervalEntry(), r.getIntervalExit(), r.getExit());
            b = TimeUtils.balanceMin(r.getEntry(), r.getIntervalEntry(), r.getIntervalExit(), r.getExit());
            bm = bm + b;
            if(b < 0) hl = hl + b;
            if(b > 0) ot = ot + b;
        }
        tvOvertime.setText(TimeUtils.toHour(ot));
        tvHourLess.setText(TimeUtils.toHour(hl));
        tvBalanceMonth.setText(TimeUtils.toHour(bm));
        tvTotalMonth.setText(TimeUtils.toHour(t));

    }

    private void delete(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int pos = menuInfo.position;
        String date = records.get(pos).getDate();
        List<Register> list = Records.getInstance().getRecords();

        for (int i = 0; i < list.size(); i++ ) {
            if (list.get(i).getDate().equals(date))
                list.remove(i);
        }

        FileUtil.saveStringToFile(Records.getFile(), Records.getInstance().getRecords().toString());

        adapter.notifyDataSetChanged();
        fillTable();
        Toast.makeText(this, "Registro deletado com sucesso.", Toast.LENGTH_SHORT).show();
    }

    private void update(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int pos = menuInfo.position;
        String date = records.get(pos).getDate();
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