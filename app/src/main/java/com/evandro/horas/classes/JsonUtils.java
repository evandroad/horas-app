package com.evandro.horas.classes;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class JsonUtils {

    private static String filePath = "horas";
    private static String fileName = "";
    private static String fileContent = "";

    public static String getJsonString(Context context, String date) {
        createNewFile(context, date);
        FileInputStream is;
        fileName = TimeUtils.getMonthYearDate(date) + ".json";
        fileContent = "";

        File evandro = new File(context.getExternalFilesDir(filePath), fileName);
        try {
            is = new FileInputStream(evandro);
            fileContent = IOUtils.toString(is);
            is.close();
        } catch (
        IOException e) {
            e.printStackTrace();
        }

        return fileContent;
    }

    public static void updateJsonFile(Context context, Records rec, String date) {

        fileContent = "";
        fileName = TimeUtils.getMonthYearDate(date) + ".json";

        for (Register reg : rec.getRecords()) {

            StringBuilder line = new StringBuilder();
            line.append("\n    {\"date\":\"" + reg.getDate() + "\",");
            line.append("\"entry\":\"" + reg.getEntry() + "\",");
            line.append("\"intervalEntry\":\"" + reg.getIntervalEntry() + "\",");
            line.append("\"intervalExit\":\"" + reg.getIntervalExit() + "\",");
            line.append("\"exit\":\"" + reg.getExit() + "\"}");

            if (fileContent == "") {
                fileContent = line.toString();
            } else {
                fileContent = fileContent + ", " + line;
            }

        }

        fileContent = "{\"records\": [" + fileContent + "]}";

        File file = new File(context.getExternalFilesDir(filePath), fileName);
        writeFile(file, fileContent);

    }

    public static void createNewFile(Context context, String date) {

        fileContent = "";
        fileName = TimeUtils.getMonthYearDate(date) + ".json";

        File folder = context.getExternalFilesDir(filePath);
        if(!folder.exists()) {
            folder.mkdir();
        }

        File file = new File(context.getExternalFilesDir(filePath), fileName);
        if (!file.exists() && !file.isFile()) {
            fileContent = "{\"records\": []}";
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
