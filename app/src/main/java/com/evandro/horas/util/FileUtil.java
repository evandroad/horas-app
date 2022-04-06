package com.evandro.horas.Util;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {

    public static String getStringFromFile(File file) {
        String content;
        FileInputStream fis;

        try {
            fis = new FileInputStream(file);
//            InputStream is = new InputStream(fis);
            content = new String ( Files.readAllBytes( Paths.get(String.valueOf(file)) ) );
            fis.close();
        } catch (IOException e) {
            content = "";
            Log.d("Horas", "Error: " + e.getMessage());
        }

        return content;
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
