package edu.temple.stockapp;

import android.database.sqlite.SQLiteOpenHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class StockDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "StockQuotes";
    private SQLiteDatabase dp;

    public StockDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(StockDBContract.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(StockDBContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    public String[] getNames(){
        final String TABLE_NAME = "entry";

        String selectQuery = "SELECT company FROM " + TABLE_NAME;
        SQLiteDatabase db  = this.getReadableDatabase();
        Cursor cursor      = db.rawQuery(selectQuery, null);
        String[] data      = null;

        if (cursor.moveToFirst()) {
            ArrayList<String> dataList=new ArrayList<>();
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndex("company"));

                dataList.add(name);
                cursor.moveToNext();
            }
            data = new String[dataList.size()];
            for(int i=0;i<data.length;i++){
                data[i]=dataList.get(i);
            }
        }
        cursor.close();
        return data;
    }
    public ArrayList<String> getSymbols(){
        final String TABLE_NAME = "entry";

        String selectQuery = "SELECT symbol FROM " + TABLE_NAME;
        SQLiteDatabase db  = this.getReadableDatabase();
        Cursor cursor      = db.rawQuery(selectQuery, null);
        String[] data      = null;
        ArrayList<String> dataList=new ArrayList<>();
        if (cursor.moveToFirst()) {

            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndex("symbol"));

                dataList.add(name);
                cursor.moveToNext();
            }

        }
        cursor.close();
        return dataList;
    }
    public String[] getPrice(){
        final String TABLE_NAME = "entry";

        String selectQuery = "SELECT price FROM " + TABLE_NAME;
        SQLiteDatabase db  = this.getReadableDatabase();
        Cursor cursor      = db.rawQuery(selectQuery, null);
        String[] data      = null;

        if (cursor.moveToFirst()) {
            ArrayList<String> dataList=new ArrayList<>();
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndex("price"));

                dataList.add(name);
                cursor.moveToNext();
            }
            data = new String[dataList.size()];
            for(int i=0;i<data.length;i++){
                data[i]=dataList.get(i);
            }
        }
        cursor.close();
        return data;
    }
    public void delete(SQLiteDatabase db){
        db.execSQL(StockDBContract.SQL_DELETE_ENTRIES);
    }



}