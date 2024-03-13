package com.example.appform.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ActivityDB";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "ActivityData";
    public static final String COL_ID = "id";
    public static final String COL_ACTIVITY_TYPE = "activityType";
    public static final String COL_ACTIVITY_TIME_MINUTES = "activityTimeMinutes";
    public static final String COL_ACTIVITY_LEVEL = "activityLevel";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_ACTIVITY_TYPE + " TEXT, " +
            COL_ACTIVITY_TIME_MINUTES + " REAL, " +
            COL_ACTIVITY_LEVEL + " TEXT" +
            ")";

    public ActivityDatabase(Context context) {
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

    // Método para inserir dados na tabela
    public void insertActivityData(String activityType, int activityTimeMinutes, String activityLevel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ACTIVITY_TYPE, activityType);
        values.put(COL_ACTIVITY_TIME_MINUTES, activityTimeMinutes);
        values.put(COL_ACTIVITY_LEVEL, activityLevel);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    @SuppressLint("Range")
    public List<HashMap<String, String>> getAllActivityData() {
        List<HashMap<String, String>> activityDataList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                HashMap<String, String> activityData = new HashMap<>();
                activityData.put("id", String.valueOf(cursor.getInt(cursor.getColumnIndex(COL_ID))));
                activityData.put("activityType", cursor.getString(cursor.getColumnIndex(COL_ACTIVITY_TYPE)));
                activityData.put("activityTimeMinutes", String.valueOf(cursor.getInt(cursor.getColumnIndex(COL_ACTIVITY_TIME_MINUTES))));
                activityData.put("activityLevel", cursor.getString(cursor.getColumnIndex(COL_ACTIVITY_LEVEL)));

                activityDataList.add(activityData);

            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();

        return activityDataList;
    }

    @SuppressLint("Range")
    public HashMap<String, String> getLastActivityData() {
        HashMap<String, String> lastActivityData = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Consulta SQL para selecionar o último registro da tabela
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL_ID + " DESC LIMIT 1";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            // Preencher o HashMap com os dados do último registro
            lastActivityData.put("id", String.valueOf(cursor.getInt(cursor.getColumnIndex(COL_ID))));
            lastActivityData.put("activityType", cursor.getString(cursor.getColumnIndex(COL_ACTIVITY_TYPE)));
            lastActivityData.put("activityTimeMinutes", String.valueOf(cursor.getInt(cursor.getColumnIndex(COL_ACTIVITY_TIME_MINUTES))));
            lastActivityData.put("activityLevel", cursor.getString(cursor.getColumnIndex(COL_ACTIVITY_LEVEL)));
            cursor.close();
        }
        db.close();

        return lastActivityData;
    }




}

