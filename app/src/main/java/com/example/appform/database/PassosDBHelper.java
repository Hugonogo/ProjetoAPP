package com.example.appform.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PassosDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PassosDB";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_PASSOS = "passos";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DATA = "data";
    public static final String COLUMN_PASSOS = "passos";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_PASSOS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_DATA + " TEXT, " +
                    COLUMN_PASSOS + " INTEGER);";

    public PassosDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PASSOS);
        onCreate(db);
    }
}

