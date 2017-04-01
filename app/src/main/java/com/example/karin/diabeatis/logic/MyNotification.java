package com.example.karin.diabeatis.logic;

/**
 * Created by Avi on 27/03/2017.
 */

public class MyNotification
{
    private int id;
    private int hour;
    private int minute;
    private boolean active;

    public MyNotification()
    {   }

    public MyNotification(int id, int hour, int minute, boolean active) {
        this.id = id;
        this.hour = hour;
        this.minute = minute;
        this.active = active;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id;   }

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

    public boolean isActive() { return active;  }

    public void setActive(boolean active) { this.active = active;   }
}
