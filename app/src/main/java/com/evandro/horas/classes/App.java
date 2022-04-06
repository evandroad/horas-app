package com.evandro.horas.classes;

import android.app.Application;
import java.io.File;

public class App extends Application {

    private static App instance;
    private static File folder;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        folder = new File(String.valueOf(getApplicationContext().getExternalFilesDir("horas")));
        Records.loadFileData();
    }

    public static App getInstance() { return instance; }

    public static File getFolder() { return folder; }

}