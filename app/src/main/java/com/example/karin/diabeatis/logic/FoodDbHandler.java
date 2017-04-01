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
        String query, query2;
        query = "CREATE TABLE Food ( FoodId INTEGER PRIMARY KEY, FoodName TEXT, FoodCarbs REAL, FoodQuantity REAL)";
        query2 = "CREATE TABLE Notification ( NotiId INT PRIMARY KEY, Hour INTEGER, Minute INTEGER, IsActive INTEGER)";
        db.execSQL(query);
        db.execSQL(query2);
        Log.d(LOGCAT,"Tables Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {    }

    public void insertFood(Food food)
    {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("FoodName", food.getFoodName());
        values.put("FoodCarbs", food.getFoodCarbs());
        values.put("FoodQuantity", food.getFoodQuantity());
        database.insert("Food", null, values);

        database.close();
    }

    public synchronized void insertNoti(MyNotification notification)
    {
        deleteNotification(notification.getId());
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("NotiId", notification.getId());
        values.put("Hour", notification.getHour());
        values.put("Minute", notification.getMinute());
        values.put("IsActive", notification.isActive());
        database.insert("Notification", null, values);

        database.close();
    }

    public void deleteFood(int id)
    {
        Log.d(LOGCAT,"delete");
        SQLiteDatabase database = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM Food where FoodId='"+ id +"'";
        Log.d("query",deleteQuery);
        database.execSQL(deleteQuery);
    }

    public void deleteNotification(int id)
    {
        Log.d(LOGCAT,"delete");
        SQLiteDatabase database = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM Notification where NotiId='"+ id +"'";
        Log.d("query",deleteQuery);
        database.execSQL(deleteQuery);
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
                f.setFoodCarbs(cursor.getDouble(2));
                f.setFoodQuantity(cursor.getDouble(3));
                // Adding contact to list
                arr.add(f);
            } while (cursor.moveToNext());
        }

        return arr;
    }

    public ArrayList<MyNotification> getAllNotifications() {
        ArrayList<MyNotification> arr = new ArrayList<MyNotification>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor  =  db.rawQuery( "select * from Notification", null );

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MyNotification notification = new MyNotification();
                notification.setId(cursor.getInt(0));
                notification.setHour(cursor.getInt(1));
                notification.setMinute(cursor.getInt(2));
                int isActive = cursor.getInt(3);
                if(isActive == 1)
                    notification.setActive(true);
                else
                    notification.setActive(false);
                // Adding contact to list
                arr.add(notification);
            } while (cursor.moveToNext());
        }

        return arr;
    }
}
