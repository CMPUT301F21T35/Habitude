package com.cmput301f21t35.habitude;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddHabitActivityTest extends TestCase {
    public void testSetHabitPlan(){
        ArrayList<String> habitPlan = new ArrayList<>() ;
        habitPlan.add("Monday");
        habitPlan.add("Tuesday");
        List<String> habitPlan1 = Arrays.asList("Monday","Tuesday");
        assertTrue("SetHabitPlan failed", habitPlan.equals(habitPlan1));
    }
}
