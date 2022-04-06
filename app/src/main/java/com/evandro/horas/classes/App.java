package com.evandro.horas.classes;

import android.app.Application;

import com.evandro.horas.util.FileUtil;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App extends Application {

    private static final String fileName = TimeUtils.getMonthYearDate(TimeUtils.getDate()) + ".json";;

    private static App instance;
    private static List<Register> data = new ArrayList<>();
    private static File folder;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        folder = new File(String.valueOf(App.getInstance().getExternalFilesDir("horas")));

        File file = new File(folder, fileName);
        Register[] records = new Gson().fromJson(FileUtil.getStringFromFile(file), Register[].class);
        if (records != null) data = Arrays.asList(records);
    }

    public static App getInstance() { return instance; }

    public static List<Register> getList() { return data; }
    public static void setList(Register r) { App.data.add(r); }

    public static File getFolder() { return folder; }

}