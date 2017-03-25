package com.example.karin.diabeatis.logic;

import java.io.Serializable;

/**
 * Created by Karin on 24/03/2017.
 */

public class Person implements Serializable
{
    private String name;
    private int age;
    private double height;
    private double weight;
    private int diabType;
    private String phone;
    private History history;
    private int totalCheck;

    public Person(String name, int age, double height, double weight, int diabType, String phone)
    {
        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.diabType = diabType;
        this.phone = phone;
        this.totalCheck = 0;
    }

    public int getTotalCheck() {
        return totalCheck;
    }

    public void setTotalCheck(int totalCheck) {
        this.totalCheck = totalCheck;
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

    public int getDiabType() {
        return diabType;
    }

    public void setDiabType(int diabType) {
        this.diabType = diabType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public History getHistory() {
        return history;
    }

    public void setHistory(History history) {
        this.history = history;
    }

}
