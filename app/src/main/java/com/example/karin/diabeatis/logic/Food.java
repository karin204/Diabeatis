package com.example.karin.diabeatis.logic;

/**
 * Created by Avi on 25/03/2017.
 */

public class Food
{
    private String foodName;
    private double foodCarbs;
    private double foodQuantity;

    public Food()
    {   }

    public Food(String foodName, double foodCarbs, double foodQuantity) {
        this.foodName = foodName;
        this.foodCarbs = foodCarbs;
        this.foodQuantity = foodQuantity;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public double getFoodCarbs() {
        return foodCarbs;
    }

    public void setFoodCarbs(double foodCarbs) {
        this.foodCarbs = foodCarbs;
    }

    public double getFoodQuantity() {
        return foodQuantity;
    }

    public void setFoodQuantity(double foodQuantity) {
        this.foodQuantity = foodQuantity;
    }

    public String toString()
    {
        return foodName + " -  פחמימות: " + foodCarbs + " כמות: " + foodQuantity;
    }
}
