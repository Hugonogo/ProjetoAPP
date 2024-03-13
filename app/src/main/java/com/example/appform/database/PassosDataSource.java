package com.example.appform.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class PassosDataSource {


    private SQLiteDatabase database;
    private PassosDBHelper dbHelper;

    public PassosDataSource(Context context) {
        dbHelper = new PassosDBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void salvarPassosDiarios(String data, int passos) {
        ContentValues values = new ContentValues();
        values.put(PassosDBHelper.COLUMN_DATA, data);
        values.put(PassosDBHelper.COLUMN_PASSOS, passos);

        database.insert(PassosDBHelper.TABLE_PASSOS, null, values);
    }

    public Cursor obterPassosDiarios() {
        String[] allColumns = { PassosDBHelper.COLUMN_ID, PassosDBHelper.COLUMN_DATA, PassosDBHelper.COLUMN_PASSOS };
        Cursor cursor = database.query(PassosDBHelper.TABLE_PASSOS, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        return cursor;
    }

}
