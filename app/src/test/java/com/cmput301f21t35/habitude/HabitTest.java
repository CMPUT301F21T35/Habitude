package com.cmput301f21t35.habitude;

import android.content.Context;

import junit.framework.TestCase;

import org.junit.Test;

import java.util.ArrayList;

public class HabitTest extends TestCase{
    public void testHabitConstruct(){
        String habitTitleName = "Running";
        String habitReason = "healthy";
        String habitStartDate = "1999-01-01";
        ArrayList<String> plan = new ArrayList<>() ;
        plan.add("Monday");
        plan.add("Tuesday");
        Boolean isPublic = true;
        int index = 1;
        Habit habit = new Habit(habitTitleName, habitReason, habitStartDate, plan, index, isPublic);
        assertTrue("Habit name is not equal", habitTitleName.equals(habit.getHabitTitleName()));
        assertTrue("Habit Reason is not equal", habitReason.equals(habit.getHabitReason()));
        assertTrue("Habit Start Date is not equal", habitStartDate.equals(habit.getHabitStartDate()));
        assertTrue("Habit plan is not equal", plan.equals(habit.getPlan()));
    }
    public void testHabitSet(){
        String habitTitleName = "Running";
        String habitTitleName1 = "Flying";
        String habitReason = "healthy";
        String habitReason1 = "thinner";
        String habitStartDate = "1999-01-01";
        String habitStartDate1 = "2000-01-01";
        ArrayList<String> plan = new ArrayList<>() ;
        plan.add("Monday");
        plan.add("Tuesday");
        ArrayList<String> plan1 = new ArrayList<>() ;
        plan.add("Saturday");
        plan.add("Sunday");
        Boolean isPublic = true;
        int index = 1;

        Habit habit = new Habit(habitTitleName,habitReason,habitStartDate,plan,index, isPublic);

        habit.setHabitTitleName("Flying");
        habit.setHabitReason("thinner");
        habit.setHabitStartDate("2000-01-01");
        habit.setPublic(true);
        habit.setPlan(plan1);
        assertTrue("set habit isPublic unsuccesfully", habit.isPublic());
        assertTrue("Set habit titlename unsuccesfully", habitTitleName1.equals(habit.getHabitTitleName()));
        assertTrue("Set habitReason unsuccefully", habitReason1.equals(habit.getHabitReason()));
        assertTrue("Set Start date unsuccessfully", habitStartDate1.equals(habit.getHabitStartDate()));
        assertTrue("Set plan unsuccessfully", plan1.equals(habit.getPlan()));




    }
}