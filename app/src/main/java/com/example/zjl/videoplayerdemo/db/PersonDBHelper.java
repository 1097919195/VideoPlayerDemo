package com.example.zjl.videoplayerdemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2018/8/11 0011.
 */

public class PersonDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "person.db";
    private static final int VERSION = 1;
    private static final String SQL_CREATE = "create table person (" +
            "name text, introduce text, id text)";
    private static final String SQL_DELETE = "drop table person";

    public PersonDBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE);
        db.execSQL(SQL_CREATE);
    }
}
