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
    private int index;
    // added is public boolean as required
    private boolean isPublic = false;
    
    public Habit(String habitTitleName,String habitReason ,String habitStartDate, ArrayList<String> plan, int index, Boolean isPublic) {
        this.habitTitleName = habitTitleName;
        this.habitReason = habitReason;
        this.habitStartDate = habitStartDate;
        this.plan = plan;
        this.index = index;
        this.isPublic = isPublic;
    }

    public Habit(String habitName, String habitReason, String habitDate, ArrayList<String> habitWeekday, Boolean isPublic) {
        this.habitTitleName = habitName;
        this.habitReason = habitReason;
        this.habitStartDate = habitDate;
        this.plan = habitWeekday;
        this.isPublic = isPublic;
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

    public void setPlan(ArrayList<String> plan) { this.plan = plan; }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

}