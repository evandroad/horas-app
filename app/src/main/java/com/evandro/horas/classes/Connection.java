package com.evandro.horas.classes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Connection extends SQLiteOpenHelper {

    private static final String name = DB.DATA_BASE;
    private static final int version = 1;

    public Connection(Context context) { super(context, name, null, version); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DB.TABLE +"(" +
            DB.DATE + " varchar(10) primary key," +
            DB.ENTRY + " varchar(5)," +
            DB.ENTRY_INT + " varchar(5), " +
            DB.EXIT_INT + " varchar(5)," +
            DB.EXIT + " varchar(5))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}