package com.cmput301f21t35.habitude;

import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.By;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class TodayPlanActivityTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<MainActivity>(MainActivity.class, true, true);

    @Before
    public void setup() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Simple test case to verify if everything's ok
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    /**
     * Add a habit in MainActivity, switch to TodayPlanActivity, test whether the habit name is consistent
     * After finishing testing, delete the habit
     */
    @Test
    public void consistentTest(){
        solo.assertCurrentActivity("Wrong", MainActivity.class);
        solo.clickOnButton("ADD Habit");

        solo.enterText((EditText)solo.getView(R.id.habitName), "Swimming");
        solo.enterText((EditText)solo.getView(R.id.habitReason), "Day off");
        solo.clickOnCheckBox(4);
        solo.setDatePicker(0,2021,11,5);
        solo.clickOnView(solo.getView(R.id.createButton));
        solo.waitForText("Swimming",1,2000);
        solo.clickOnView(solo.getView(R.id.action_today));
        solo.waitForActivity("TodayPlanActivity");
        assertTrue(solo.searchText("Swimming"));
        //delete the habit
        solo.clickOnView(solo.getView(R.id.action_habits));
        solo.waitForActivity("MainActivity");
        solo.clickLongOnText("Swimming");
        solo.clickOnText("YES");
    }
}
