package com.example.udhari;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class _3_DataBase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "my_database.db";
    private static final String TABLE_NAME = "my_table";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_NUMBER = "number";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_GSON_STRING = "gson_string";

    public _3_DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_NUMBER + " TEXT, " +
                COLUMN_AMOUNT + " INTEGER, " +
                COLUMN_GSON_STRING + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addData(String name, String number, Long amount, ArrayList<_2_TransactionInfo> myObjects) {
        SQLiteDatabase db = getWritableDatabase();
        Gson gson = new Gson();
        String gsonString = gson.toJson(myObjects);
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_NUMBER, number);
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_GSON_STRING, gsonString);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<_1_CompleteInfo> getAllData() {
        ArrayList<_1_CompleteInfo> data = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectAllQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectAllQuery, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                @SuppressLint("Range") String number = cursor.getString(cursor.getColumnIndex(COLUMN_NUMBER));
                @SuppressLint("Range") Long amount = cursor.getLong(cursor.getColumnIndex(COLUMN_AMOUNT));
                @SuppressLint("Range") String gsonString = cursor.getString(cursor.getColumnIndex(COLUMN_GSON_STRING));
                Gson gson = new Gson();
                ArrayList<_2_TransactionInfo> myObjects = gson.fromJson(gsonString, new TypeToken<ArrayList<_2_TransactionInfo>>(){}.getType());
                ArrayList<_2_TransactionInfo> myObjectList = new ArrayList<>();
                for (_2_TransactionInfo myObject : myObjects) {
                    myObjectList.add(myObject);
                }
                _1_CompleteInfo myData = new _1_CompleteInfo(name, number, amount, myObjectList);
                data.add(myData);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return data;
    }
    public void updateData(String name, String number, Long amount, ArrayList<_2_TransactionInfo> myObjects) {
        SQLiteDatabase db = getWritableDatabase();
        Gson gson = new Gson();
        String gsonString = gson.toJson(myObjects);
        ContentValues values = new ContentValues();
        values.put(COLUMN_NUMBER, number);
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_GSON_STRING, gsonString);
        db.update(TABLE_NAME, values, COLUMN_NAME + " = ?", new String[]{name});
        db.close();
    }
    public void deleteData(String name) {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLE_NAME, COLUMN_NAME + " = ?", new String[]{name});
        db.close();
    }

}
