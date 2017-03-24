package com.example.karin.diabeatis.logic;

/**
 * Created by Karin on 24/03/2017.
 */

public class Person
{
    private String name;
    private int age;
    private double height;
    private double weight;
    private char diabType;
    private History history;

    public Person(String name, int age, double height,double weight,char diabType)
    {
        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.diabType = diabType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public char getDiabType() {
        return diabType;
    }

    public void setDiabType(char diabType) {
        this.diabType = diabType;
    }

    public History getHistory() {
        return history;
    }

    public void setHistory(History history) {
        this.history = history;
    }

}
