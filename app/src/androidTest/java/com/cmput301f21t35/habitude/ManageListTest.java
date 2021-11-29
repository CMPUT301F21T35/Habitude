package com.cmput301f21t35.habitude;

import android.app.Activity;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(AndroidJUnit4.class)
public class ManageListTest {
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

    public void enterSampleHabit(String habitTitle) {
        RecyclerView habitList = (RecyclerView) solo.getView(R.id.habit_list);
        RecyclerView.Adapter habitDataList = habitList.getAdapter();
        MainActivity mainActivity = MainActivity.getInstance();

        for (int index = 0; index < mainActivity.habitDataList.size(); index++) {
            Habit testingHabit = (Habit) mainActivity.habitDataList.get(index);
            if (testingHabit.getHabitTitleName().equals(habitTitle)) {
                solo.clickInList(index, 0);
                break;
            }
        }
    }

    public void deleteSampleHabit(String habitTitle) {
        RecyclerView habitList = (RecyclerView) solo.getView(R.id.habit_list);
        RecyclerView.Adapter habitDataList = habitList.getAdapter();
        MainActivity mainActivity = MainActivity.getInstance();

        for (int index = 0; index < habitDataList.getItemCount(); index++) {
            Habit testingHabit = (Habit) mainActivity.habitDataList.get(index);
            if (testingHabit.getHabitTitleName().equals(habitTitle)) {
                solo.clickLongInList(index, 0);
                break;
            }
        }
    }

    public boolean findSampleHabit(String habitTitle) {
        RecyclerView habitList = (RecyclerView) solo.getView(R.id.habit_list);
        RecyclerView.Adapter habitDataList = habitList.getAdapter();
        MainActivity mainActivity = MainActivity.getInstance();

        for (int index = 0; index < mainActivity.habitDataList.size(); index++) {
            Habit testingHabit = (Habit) mainActivity.habitDataList.get(index);
            if (testingHabit.getHabitTitleName().equals(habitTitle)) {
                return true;
            }
        }
        return false;
    }

    //Swipes right on a given item
    public void swipeRight(String habitTitle) {
        //int[] location = new int[2];
        View row = solo.getText(habitTitle);
        //row.getLocationInWindow(location);
        solo.scrollViewToSide(row,Solo.LEFT,10,10);
        //return location;
    }

    //Gets the location of a given item
    public void swipeUp(String habitTitle) {
        //int[] location = new int[2];
        View row = solo.getText(habitTitle);
        //row.getLocationInWindow(location);
        solo.scrollViewToSide(row,Solo.UP,10,10);
        //return location;
    }

    //Adds an event iff it does not exist
    public void filterAddEventTest(String habitTitle) {
        if (!findSampleHabit(habitTitle)) {
            addEventForTesting(habitTitle);
        }
    }

    /**
     * Create an activity to edit
     */
    public void addEventForTesting(String habitTitle) {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnButton(0);
        EditText nameText = (EditText) solo.getView(R.id.habitName);
        solo.enterText(nameText, habitTitle);
        EditText reasonText = (EditText) solo.getView(R.id.habitReason);
        solo.enterText(reasonText, "A sample reason");
        solo.clickOnCheckBox(0);
        solo.clickOnCheckBox(4);
        solo.setDatePicker(0,1999,8,8);
        solo.clickOnView(solo.getView(R.id.createButton));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    //Gets the count of items in the list.
    //Note that this code is going to be very inefficient.
    public int listCount() {
        RecyclerView habitList = (RecyclerView) solo.getView(R.id.habit_list);
        RecyclerView.Adapter habitDataList = habitList.getAdapter();
        MainActivity mainActivity = MainActivity.getInstance();
        return habitDataList.getItemCount();
    }

    //Empties out the list.
    public void emptyList() {
        MainActivity mainActivity = MainActivity.getInstance();
        for (int i = 0; i < listCount(); i++) {
            String nextTitle = mainActivity.habitDataList.get(i).getHabitTitleName();
            swipeRight(nextTitle);
        }
    }

    @Test
    public void deleteTest() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        assertEquals(listCount(),0);
        filterAddEventTest("zero");
        int[] location = new int[2];
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        swipeRight("zero");
        assertEquals(listCount(),0);
    }

    @Test
    public void reorderTest() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        emptyList();
        filterAddEventTest("zero");
        filterAddEventTest("one");
        swipeUp("zero");
        emptyList();
    }
}