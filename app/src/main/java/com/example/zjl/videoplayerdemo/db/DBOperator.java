package com.example.zjl.videoplayerdemo.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.zjl.videoplayerdemo.bean.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/8/11 0011.
 */

public class DBOperator {
    private static final String SQL_INSERT = "insert into person (name, introduce, id) values (?, ?, ?)";
    private static final String SQL_DELETE = "delete from person where name = ?";
    private static final String SQL_UPDATE = "update person set id = ? where person = ?";
    private static final String SQL_SELECT = "select * from person where id = ?";

    private PersonDBHelper dbHelper;

    public DBOperator(Context context) {
        dbHelper = new PersonDBHelper(context);
    }

    public void insert(Person person) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(SQL_INSERT, new Object[] {
                person.getName(), person.getIntroduce(), person.getId()
        });
        db.close();
    }

    public void delete(String name) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(SQL_DELETE, new Object[] {
                name
        });
        db.close();
    }

    public void update(String label, String name) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(SQL_UPDATE, new Object[]{
                label, name
        });
        db.close();
    }

    public List<Person> select(String id) {
        ArrayList<Person> persons = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(SQL_SELECT, new String[]{
                id
        });
        int nameCol = cursor.getColumnIndex("name");
        int introduceCol = cursor.getColumnIndex("introduce");
        while (cursor.moveToNext()) {
            String name = cursor.getString(nameCol);
            String introduce = cursor.getString(introduceCol);
            Person person = new Person(name, introduce, id);
            persons.add(person);
        }
        cursor.close();
        db.close();

        return persons;
    }

    public boolean isRecordEmpty(String label) {
        List<Person> persons = select(label);
        if (persons == null) {
            return true;
        }
        return false;
    }
}
