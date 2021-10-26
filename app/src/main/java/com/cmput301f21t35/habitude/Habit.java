package com.cmput301f21t35.habitude;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

public class Habit implements Serializable {
    private String habitTitleName;
    private String habitReason;
    private Date habitStartDate;
    private HashMap<Integer, Boolean> habitHash;

    public Habit(String habitTitleName,String habitReason ,Date habitStartDate, HashMap<Integer, Boolean> habitHash) {
        this.habitTitleName = habitTitleName;
        this.habitReason = habitReason;
        this.habitStartDate = habitStartDate;
        this.habitHash = habitHash;
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

    public Date getHabitStartDate() {
        return habitStartDate;
    }

    public void setHabitStartDate(Date habitStartDate) {
        this.habitStartDate = habitStartDate;
    }

    public HashMap<Integer, Boolean> getHabitHash() {
        return habitHash;
    }

    public void setHabitHash(HashMap<Integer, Boolean> habitHash) {
        this.habitHash = habitHash;
    }
}
