package com.example.karin.diabeatis.logic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Karin on 27/03/2017.
 */

public class CheckDbHandler extends SQLiteOpenHelper {

    private static final String LOGCAT = null;
    private static CheckDbHandler cDbInstance;


    public static synchronized CheckDbHandler getInstance(Context context) {

        if (cDbInstance == null) {
            cDbInstance = new CheckDbHandler(context.getApplicationContext());
        }
        return cDbInstance;
    }


    private CheckDbHandler(Context applicationcontext)
    {
        super(applicationcontext, "examDB.db", null, 1);
        Log.d(LOGCAT,"Exam DB Created");
    }


    @Override
    public void onCreate(SQLiteDatabase db2) {
        String queryC;
        queryC = "CREATE TABLE Exam ( ExamId INTEGER PRIMARY KEY, ExamValue REAL, PersonName TEXT)";
        db2.execSQL(queryC);
        Log.d(LOGCAT,"Exam Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query;
        query = "DROP TABLE IF EXISTS Exam";
        db.execSQL(query);
        onCreate(db);
    }

    public void insertCheck(Check newCheck)
    {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("ExamValue", newCheck.getCheck());
        values.put("PersonName", newCheck.getName());
        database.insert("Exam", null, values);
        database.close();
    }


    public ArrayList<Check> getAllChecks() {
        ArrayList<Check> checks = new ArrayList<Check>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor  =  db.rawQuery( "select * from Exam", null );

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Check ch = new Check();
                ch.setCheck(cursor.getDouble(1));
                ch.setName(cursor.getString(2));
                // Adding contact to list
                checks.add(ch);
            } while (cursor.moveToNext());
        }

        return checks;
    }

}
