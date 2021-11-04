package com.cmput301f21t35.habitude;

import android.app.Activity;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
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

    /**
     * Create an activity to edit
     */
    @Test
    public void addEventTest() {
        //Create a temporary event
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnButton(0);
        EditText nameText = (EditText) solo.getView(R.id.habitName);
        solo.enterText(nameText, "sample habit");
        EditText reasonText = (EditText) solo.getView(R.id.habitReason);
        solo.enterText(reasonText, "sample reason");
        solo.clickOnCheckBox(0);
        solo.clickOnCheckBox(4);
        solo.setDatePicker(0,1999,8,8);
        solo.clickOnButton(7);
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    @Test
    public void viewEventTest() {
        addEventTest(); //check equal
        solo.clickInList(3,0); //How to choose the right one?
        EditText nameText = (EditText) solo.getView(R.id.habit_title);
        assertEquals("sample habit",nameText.getText().toString());
        EditText reasonText = (EditText) solo.getView(R.id.habit_description);
        assertEquals("sample reason",reasonText.getText().toString());
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
        solo.clickInList(3,0); //How to choose the right one?
        //...
    }

    @Test
    public void deleteEventTest(){
        viewEventTest();
        ListView habitTesting = (ListView) solo.getView(R.id.habit_list);
        int lengthBefore = habitTesting.getCount();
        solo.clickLongInList(3,0); //How to choose the right one?
        solo.waitForDialogToOpen();
        solo.clickOnText("YES");
        solo.waitForDialogToClose();
        int lengthAfter = habitTesting.getCount();
        assertEquals(1,lengthBefore-lengthAfter);
    }
}