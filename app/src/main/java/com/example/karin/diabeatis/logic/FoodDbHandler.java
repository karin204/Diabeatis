package com.example.karin.diabeatis.logic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Avi on 07/01/2017.
 */

public class FoodDbHandler extends SQLiteOpenHelper{
    private static final String LOGCAT = null;
    private static FoodDbHandler fDbInstance;

    public static synchronized FoodDbHandler getInstance(Context context) {

        if (fDbInstance == null) {
            fDbInstance = new FoodDbHandler(context.getApplicationContext());
        }
        return fDbInstance;
    }

    private FoodDbHandler(Context applicationcontext)
    {
        super(applicationcontext, "foodDB.db", null, 1);
        Log.d(LOGCAT,"Food DB Created");
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String query;
        query = "CREATE TABLE Food ( FoodId INTEGER PRIMARY KEY, FoodName TEXT, FoodCal REAL, FoodQuantity REAL)";
        db.execSQL(query);
        Log.d(LOGCAT,"HighScores Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        String query;
        query = "DROP TABLE IF EXISTS Food";
        db.execSQL(query);
        onCreate(db);
    }

    public void insertFood(Food food)
    {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("FoodName", food.getFoodName());
        values.put("FoodCal", food.getFoodCal());
        values.put("FoodQuantity", food.getFoodQuantity());
        database.insert("Food", null, values);

        database.close();
    }


    public ArrayList<Food> getAllFoods() {
        ArrayList<Food> arr = new ArrayList<Food>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor  =  db.rawQuery( "select * from Food", null );

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Food f = new Food();
                f.setFoodName(cursor.getString(1));
                f.setFoodCal(cursor.getDouble(2));
                f.setFoodQuantity(cursor.getDouble(2));
                // Adding contact to list
                arr.add(f);
            } while (cursor.moveToNext());
        }

        return arr;
    }
}
