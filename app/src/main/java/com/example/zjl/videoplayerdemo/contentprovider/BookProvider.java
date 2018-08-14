package com.example.zjl.videoplayerdemo.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2018/8/13 0013.
 */

public class BookProvider extends ContentProvider {

    private Context context;

    private SQLiteDatabase sqLiteDatabase;

    public static final String AUTHORITY = "com.zjl.contentproviderdemo.BookProvider";

    public static final int BOOK_URI_CODE = 0;

    public static final int USER_URI_CODE = 1;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, DbOpenHelper.BOOK_TABLE_NAME, BOOK_URI_CODE);
        uriMatcher.addURI(AUTHORITY, DbOpenHelper.USER_TABLE_NAME, USER_URI_CODE);
    }

    private String getTableName(Uri uri) {
        String tableName = null;
        switch (uriMatcher.match(uri)) {
            case BOOK_URI_CODE:
                tableName = DbOpenHelper.BOOK_TABLE_NAME;
                break;
            case USER_URI_CODE:
                tableName = DbOpenHelper.USER_TABLE_NAME;
                break;
        }
        return tableName;
    }

    public BookProvider() {

    }

    @Override
    public boolean onCreate() {
        context = getContext();
        initProviderData();
        return false;
    }

    //初始化原始数据
    private void initProviderData() {
        sqLiteDatabase = new DbOpenHelper(context).getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        ContentValues contentValues = new ContentValues();
        contentValues.put("bookName", "数据结构");
        sqLiteDatabase.insert(DbOpenHelper.BOOK_TABLE_NAME, null, contentValues);
        contentValues.put("bookName", "编译原理");
        sqLiteDatabase.insert(DbOpenHelper.BOOK_TABLE_NAME, null, contentValues);
        contentValues.put("bookName", "网络原理");
        sqLiteDatabase.insert(DbOpenHelper.BOOK_TABLE_NAME, null, contentValues);
        contentValues.clear();

        contentValues.put("userName", "叶");
        contentValues.put("sex", "女");
        sqLiteDatabase.insert(DbOpenHelper.USER_TABLE_NAME, null, contentValues);
        contentValues.put("userName", "叶叶");
        contentValues.put("sex", "男");
        sqLiteDatabase.insert(DbOpenHelper.USER_TABLE_NAME, null, contentValues);
        contentValues.put("userName", "叶应是叶");
        contentValues.put("sex", "男");
        sqLiteDatabase.insert(DbOpenHelper.USER_TABLE_NAME, null, contentValues);
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String tableName = getTableName(uri);
        if (tableName == null) {
            throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        sqLiteDatabase.insert(tableName, null, values);
        context.getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String tableName = getTableName(uri);
        if (tableName == null) {
            throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        return sqLiteDatabase.query(tableName, projection, selection, selectionArgs, null, null, sortOrder, null);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String tableName = getTableName(uri);
        if (tableName == null) {
            throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        int row = sqLiteDatabase.update(tableName, values, selection, selectionArgs);
        if (row > 0) {
            context.getContentResolver().notifyChange(uri, null);
        }
        return row;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String tableName = getTableName(uri);
        if (tableName == null) {
            throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        int count = sqLiteDatabase.delete(tableName, selection, selectionArgs);
        if (count > 0) {
            context.getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

}