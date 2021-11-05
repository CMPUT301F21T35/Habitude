package com.cmput301f21t35.habitude;

import android.app.Activity;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.robotium.solo.Solo;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class EditAndDeleteActivityTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @After
    public void cleanUp() {
        deleteSampleHabit("A sample habit");
        deleteSampleHabit("A sample habit 2");
    }

    /**
     * Gets the Activity
     *
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    //We use this to enter a habit from the listView based on its name if it exists.
    public void enterSampleHabit(String habitTitle) {
        ListView habitList = (ListView) solo.getView(R.id.habit_list);
        ListAdapter habitDataList = habitList.getAdapter();
        for (int index = 0; index < habitDataList.getCount(); index++) {
            Habit testingHabit = (Habit) habitDataList.getItem(index);
            if (testingHabit.getHabitTitleName().equals(habitTitle)) {
                solo.clickInList(index, 0);
                break;
            }
        }
    }

    //We use this to delete a habit from the listView based on its name if it exists.
    public void deleteSampleHabit(String habitTitle) {
        ListView habitList = (ListView) solo.getView(R.id.habit_list);
        ListAdapter habitDataList = habitList.getAdapter();
        for (int index = 0; index < habitDataList.getCount(); index++) {
            Habit testingHabit = (Habit) habitDataList.getItem(index);
            if (testingHabit.getHabitTitleName().equals(habitTitle)) {
                solo.clickLongInList(index, 0);
                break;
            }
        }
    }

    //We use this to check if a habit exists in the listView.
    public boolean findSampleHabit(String habitTitle) {
        ListView habitList = (ListView) solo.getView(R.id.habit_list);
        ListAdapter habitDataList = habitList.getAdapter();
        for (int index = 0; index < habitDataList.getCount(); index++) {
            Habit testingHabit = (Habit) habitDataList.getItem(index);
            if (testingHabit.getHabitTitleName().equals(habitTitle)) {
                return true;
            }
        }
        return false;
    }

    //We do this to do the "add a habit for testing" routine if it already exists.
    public void filterAddEventTest() {
        if (!findSampleHabit("A sample habit")) {
            addEventTest();
        }
    }

    /**
     * Create an activity to edit
     */
    @Test
    public void addEventTest() {
        //Create a temporary event
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnButton(0);
        EditText nameText = (EditText) solo.getView(R.id.habitName);
        solo.enterText(nameText, "A sample habit");
        EditText reasonText = (EditText) solo.getView(R.id.habitReason);
        solo.enterText(reasonText, "A sample reason");
        solo.clickOnCheckBox(0);
        solo.clickOnCheckBox(4);
        solo.setDatePicker(0,1999,8,8);
        solo.clickOnButton(7);
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    @Test
    public void viewEventTest() {
        filterAddEventTest();
        //Get into the event test
        enterSampleHabit("A sample habit");
        //Validate everything
        EditText nameText = (EditText) solo.getView(R.id.habit_title);
        assertEquals("A sample habit",nameText.getText().toString());
        EditText reasonText = (EditText) solo.getView(R.id.habit_description);
        assertEquals("A sample reason",reasonText.getText().toString());
        ToggleButton mondayButton = (ToggleButton) solo.getView(R.id.monday_button);
        assertTrue(mondayButton.isChecked());
        ToggleButton fridayButton = (ToggleButton) solo.getView(R.id.friday_button);
        assertTrue(fridayButton.isChecked());
        ToggleButton wednesdayButton = (ToggleButton) solo.getView(R.id.wednesday_button);
        assertFalse(wednesdayButton.isChecked());
        DatePicker calendarDisplay = (DatePicker) solo.getView(R.id.habit_calendar);
        assertEquals(1999,calendarDisplay.getYear());
        assertEquals(7,calendarDisplay.getMonth());
        assertEquals(8,calendarDisplay.getDayOfMonth());
        //Leave
        solo.clickOnButton(7);
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    @Test
    public void editEventTest(){
        filterAddEventTest();
        //change all the details
        enterSampleHabit("A sample habit");
        EditText nameText = (EditText) solo.getView(R.id.habit_title);
        solo.enterText(nameText, " 2");
        EditText reasonText = (EditText) solo.getView(R.id.habit_description);
        solo.enterText(reasonText, " 2");
        solo.setDatePicker(0,1999,10,11);
        //Change Friday to Wednesday
        solo.clickOnButton(2);
        solo.clickOnButton(4);
        //Exit
        solo.clickOnButton(7);
        //And reenter
        enterSampleHabit("A sample habit 2");
        assertEquals("A sample habit 2",nameText.getText().toString());
        assertEquals("A sample reason 2",reasonText.getText().toString());
        ToggleButton mondayButton = (ToggleButton) solo.getView(R.id.monday_button);
        assertTrue(mondayButton.isChecked());
        ToggleButton fridayButton = (ToggleButton) solo.getView(R.id.friday_button);
        assertFalse(fridayButton.isChecked());
        ToggleButton wednesdayButton = (ToggleButton) solo.getView(R.id.wednesday_button);
        assertTrue(wednesdayButton.isChecked());
        DatePicker calendarDisplay = (DatePicker) solo.getView(R.id.habit_calendar);
        assertEquals(1999,calendarDisplay.getYear());
        assertEquals(10,calendarDisplay.getMonth());
        assertEquals(11,calendarDisplay.getDayOfMonth());
        solo.clickOnButton(7);
    }

    @Test
    public void deleteEventTest(){
        filterAddEventTest();
        ListView habitTesting = (ListView) solo.getView(R.id.habit_list);
        int lengthBefore = habitTesting.getCount();
        deleteSampleHabit("A sample habit");
        solo.waitForDialogToOpen();
        solo.clickOnText("YES");
        solo.waitForDialogToClose();
        int lengthAfter = habitTesting.getCount();
        assertEquals(1,lengthBefore-lengthAfter);
    }
}
