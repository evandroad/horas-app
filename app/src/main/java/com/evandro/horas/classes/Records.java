package com.evandro.horas.classes;

import com.evandro.horas.util.FileUtil;
import com.evandro.horas.util.TimeUtils;
import com.google.gson.Gson;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Records {


    private static Records instance = new Records();

    private List<Register> records;

    private Records() { this.records = new ArrayList<>(); }

    public static Records getInstance() { return instance; }

    public List<Register> getRecords() { return records; }
    public void setRecords(Register reg) { this.records.add(reg); }

    private static String getFileName() { return TimeUtils.getMonthYearDate(TimeUtils.getDate()) + ".json"; }

    public static File getFile() { return new File(App.getFolder(), getFileName()); }

    public static void clear() { instance.getRecords().clear(); }

    @Override
    public String toString() {

        String all = "";

        for(Register r : records) {
            all += "\n" + r + ",";
        }
        all = all.replaceFirst(".$", "");

        return "[" + all + "\n]\n";
    }

    public void save() {
        createFile();
        FileUtil.saveStringToFile(getFile(), records.toString());
    }

    private void createFile() {
        if (!App.getFolder().exists())
            App.getFolder().mkdir();

        if (!getFile().exists()) {
            FileUtil.saveStringToFile(getFile(), "[]\n");
        }
    }

    public static void loadFileData() {
        if (instance != null) instance.clear();
        instance.createFile();
        Register[] rec = new Gson().
                fromJson(FileUtil.getStringFromFile(getFile()), Register[].class);
        if (rec != null) {
            for ( Register r : rec ) {
                instance.setRecords(r);
            }
        }
    }

}