package com.cmput301f21t35.habitude;

import android.app.Activity;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

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
public class EditActivityTest {
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
    /**
     * Gets the Activity
     *
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    public void findSampleHabit() {
        ListView habitList = (ListView) solo.getView(R.id.habit_list);
        ListAdapter habitDataList = habitList.getAdapter();
        for (int index = 0; index < habitDataList.getCount(); index++) {
            //if (habitDataList.getItem(index).getHabitTitleName() == "sample habit") {
                solo.clickInList(index,0);
                break;
            //}
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
        addEventTest(); //check equal
        solo.clickInList(0,0); //How to choose the right one?
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
        solo.clickOnButton(7);
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    @Test
    public void editEventTest(){
        viewEventTest();
        //change all the details
        solo.clickInList(0,0); //How to choose the right one?
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
        solo.clickInList(0,0); //How to choose the right one?
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
        viewEventTest();
        ListView habitTesting = (ListView) solo.getView(R.id.habit_list);
        int lengthBefore = habitTesting.getCount();
        solo.clickLongInList(0,0); //How to choose the right one?
        solo.waitForDialogToOpen();
        solo.clickOnText("YES");
        solo.waitForDialogToClose();
        int lengthAfter = habitTesting.getCount();
        assertEquals(1,lengthBefore-lengthAfter);
    }
}
