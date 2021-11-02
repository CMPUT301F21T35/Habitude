package com.cmput301f21t35.habitude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Habit implements Serializable {
    private String habitTitleName;
    private String habitReason;
    private String habitStartDate;
    private ArrayList<String> plan;

    public Habit(String habitTitleName,String habitReason ,String habitStartDate, ArrayList<String> plan) {
        this.habitTitleName = habitTitleName;
        this.habitReason = habitReason;
        this.habitStartDate = habitStartDate;
        this.plan = plan;
    }

    public String getHabitTitleName() {
        return habitTitleName;
    }

    public void setHabitTitleName(String habitTitleName) {
        this.habitTitleName = habitTitleName;
    }

    public String getHabitReason() {
        return habitReason;
    }

    public void setHabitReason(String habitReason) {
        this.habitReason = habitReason;
    }

    public String getHabitStartDate() {
        return habitStartDate;
    }

    public void setHabitStartDate(String habitStartDate) {
        this.habitStartDate = habitStartDate;
    }

    public ArrayList<String> getPlan() {
        return plan;
    }

    public void setHabitHash(ArrayList<String> plan) {
        this.plan = plan;
    }
}
