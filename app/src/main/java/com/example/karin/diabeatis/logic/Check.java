package com.example.karin.diabeatis.logic;

/**
 * Created by Karin on 27/03/2017.
 */

public class Check {
    private double check;
    private String name;


    public Check()
    {   }


    public Check(String name,double check)
    {
        this.name = name;
        this.check = check;
    }

    public double getCheck() {
        return check;
    }

    public void setCheck(double check) {
        this.check = check;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
