package com.evandro.horas.classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class RegisterDAO {

    private Connection connection;
    private SQLiteDatabase database;

    public RegisterDAO(Context context) {
        connection = new Connection(context);
        database = connection.getWritableDatabase();
    }

    public long add(Register register) {
        ContentValues values = new ContentValues();
        values.put(DB.DATE, register.getDate());
        values.put(DB.ENTRY, register.getEntry());
        values.put(DB.ENTRY_INT, register.getIntervalEntry());
        values.put(DB.EXIT_INT, register.getIntervalExit());
        values.put(DB.EXIT, register.getExit());

        return database.insert(DB.TABLE, null, values);
    }

    public List<Register> getRecords() {
        List<Register> records = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + DB.TABLE, null);

        while (cursor.moveToNext()) {
            Register r = new Register();
            r.setDate(cursor.getString(0));
            r.setEntry(cursor.getString(1));
            r.setIntervalEntry(cursor.getString(2));
            r.setIntervalExit(cursor.getString(3));
            r.setExit(cursor.getString(4));
            records.add(r);
        }
        return records;
    }

}