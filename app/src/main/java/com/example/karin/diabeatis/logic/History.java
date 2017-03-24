package com.example.karin.diabeatis.logic;

import java.util.ArrayList;

/**
 * Created by Karin on 24/03/2017.
 */

public class History
{

    private ArrayList<Double> checks = new ArrayList<>();
    private double lastCheck;

    public ArrayList<Double> getChecks() {
        return checks;
    }

    public void setChecks(ArrayList<Double> checks) {
        this.checks = checks;
    }

    public double getLastCheck() {
        return lastCheck;
    }

    public void setLastCheck(double lastCheck) {
        this.lastCheck = lastCheck;
    }

}
