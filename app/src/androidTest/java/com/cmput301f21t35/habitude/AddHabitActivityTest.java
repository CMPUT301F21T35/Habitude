package com.cmput301f21t35.habitude;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class AddHabitActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<AddHabitActivity> rule = new ActivityTestRule<>(AddHabitActivity.class,true,true);
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

    }
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }
    @Test
    public void checkCreate(){
        solo.assertCurrentActivity("Wrong Activity", AddHabitActivity.class);


        solo.enterText((EditText)solo.getView(R.id.habitName),"Flying");
        solo.enterText((EditText)solo.getView(R.id.habitReason), "keep healthy");

        solo.clickOnToggleButton("MON");
        solo.clickOnToggleButton("TUES");

        solo.setDatePicker(0,1999,8,8);

        solo.clickOnButton("CREATE");
        solo.finishOpenedActivities();
        assertFalse(solo.searchText("Flying"));










    }
}
