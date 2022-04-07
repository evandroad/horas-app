package com.evandro.horas.util;

import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {

    public static String getStringFromFile(File file) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                content.append(currentLine).append("\n");
            }
        } catch (IOException e) {
            Log.d("Horas", "Error: " + e.getMessage());
        }

        return content.toString();
    }

    public static void saveStringToFile(File file, String content) {
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
