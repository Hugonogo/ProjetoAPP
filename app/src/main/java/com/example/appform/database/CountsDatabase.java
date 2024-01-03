package com.example.appform.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CountsDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AcelerometroDB";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "AcelerometroData";
    public static final String COL_ID = "id";
    public static final String COL_X = "xValue";
    public static final String COL_Y = "yValue";
    public static final String COL_Z = "zValue";
    public static final String COL_TIMESTAMP = "timestamp";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_X + " REAL, " +
            COL_Y + " REAL, " +
            COL_Z + " REAL, " +
            COL_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
            ")";
    public CountsDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertData(double xValue, double yValue, double zValue) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_X, xValue);
        values.put(COL_Y, yValue);
        values.put(COL_Z, zValue);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
}
