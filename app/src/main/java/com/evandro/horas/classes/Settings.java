package com.evandro.horas.classes;

import android.content.Context;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Settings {

    String startDate;
    private static String filePath = "settings/";
    private static String fileName = "settings.json";
    private static String fileContent = "";

    public static void createNewFile(Context context) {

        fileContent = "";

        File folder = context.getExternalFilesDir(filePath);
        if(!folder.exists()) {
            folder.mkdir();
        }

        File file = new File(context.getExternalFilesDir(filePath), fileName);
        if (!file.exists() && !file.isFile()) {
            fileContent = "{\"startDay\":\"16\"}";
            writeFile(file, fileContent);
        }
    }

    private static void writeFile(File file, String content) {
        FileWriter writer;
        try {
            writer = new FileWriter(file);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
