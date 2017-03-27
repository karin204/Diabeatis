package com.example.karin.diabeatis.logic;

/**
 * Created by Avi on 25/03/2017.
 */

public class Food
{
    private String foodName;
    private double foodCal;
    private double foodQuantity;

    public Food()
    {   }

    public Food(String foodName, double foodCal, double foodQuantity) {
        this.foodName = foodName;
        this.foodCal = foodCal;
        this.foodQuantity = foodQuantity;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public double getFoodCal() {
        return foodCal;
    }

    public void setFoodCal(double foodCal) {
        this.foodCal = foodCal;
    }

    public double getFoodQuantity() {
        return foodQuantity;
    }

    public void setFoodQuantity(double foodQuantity) {
        this.foodQuantity = foodQuantity;
    }

    public String toString()
    {
        return foodName + " -  כלוריות: " + foodCal + " כמות: " + foodQuantity;
    }
}
