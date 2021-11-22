package com.evandro.horas.classes;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.evandro.horas.R;

import java.text.DecimalFormat;
import java.util.List;

public class RegisterAdapter extends BaseAdapter {

    private List<Register> records;
    private Activity activity;

    public RegisterAdapter(Activity activity, List<Register> records) {
        this.activity = activity;
        this.records = records;
    }

    @Override
    public int getCount() {
        return records.size();
    }

    @Override
    public Object getItem(int i) {
        return records.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = activity.getLayoutInflater().inflate(R.layout.item, viewGroup, false);
        TextView date = v.findViewById(R.id.tvDate);
        TextView entry = v.findViewById(R.id.tvEntry);
        TextView intervalEntry = v.findViewById(R.id.tvIntervalEntry);
        TextView intervalExit = v.findViewById(R.id.tvIntervalExit);
        TextView exit = v.findViewById(R.id.tvExit);
        TextView balance = v.findViewById(R.id.tvBalance);
        TextView total = v.findViewById(R.id.tvTotal);

        Register r = records.get(i);

        date.setText(r.getDate());
        entry.setText(r.getEntry());
        intervalEntry.setText(r.getIntervalEntry());
        intervalExit.setText(r.getIntervalExit());
        exit.setText(r.getExit());

        int min = TimeUtils.balanceMin(r.getEntry(), r.getIntervalEntry(), r.getIntervalExit(), r.getExit());
        balance.setText(TimeUtils.toHour(min));

        int totMin = TimeUtils.totalMin(r.getEntry(), r.getIntervalEntry(), r.getIntervalExit(), r.getExit());
        total.setText(TimeUtils.toHour(totMin));

        return v;

    }

}
