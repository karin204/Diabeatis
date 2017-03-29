package com.example.karin.diabeatis.logic;

/**
 * Created by Avi on 27/03/2017.
 */

public class MyNotification
{
    private int hour;
    private int minute;
    private boolean active;

    public MyNotification(int hour, int minute, boolean active) {
        this.hour = hour;
        this.minute = minute;
        this.active = active;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String toString(){
        return (hour + ":" + minute);
    }
}
