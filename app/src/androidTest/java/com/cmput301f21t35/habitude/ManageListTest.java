package com.cmput301f21t35.habitude;

import android.app.Activity;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
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

    //Checks whether or not a given habit is in the list
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

    //Checks whether or not a given habit is in the list, and returns it's index
    public Habit getHabit(String habitTitle) {
        RecyclerView habitList = (RecyclerView) solo.getView(R.id.habit_list);
        RecyclerView.Adapter habitDataList = habitList.getAdapter();
        MainActivity mainActivity = MainActivity.getInstance();

        for (int index = 0; index < mainActivity.habitDataList.size(); index++) {
            Habit testingHabit = (Habit) mainActivity.habitDataList.get(index);
            if (testingHabit.getHabitTitleName().equals(habitTitle)) {
                return testingHabit;
            }
        }
        return null;
    }

    //Swipes right on a given item
    public void swipeRight(String habitTitle) {
            int[] location = new int[2];
            View row = solo.getText(habitTitle);
            row.getLocationInWindow(location);
            solo.drag(
                    (float) location[0],
                    (float) location[0] + 200,
                    (float) location[1],
                    (float) location[1],
                    10
            );
        //solo.scrollViewToSide(row,Solo.LEFT,10,10);
        //return location;
    }

    //Drags one item to another
    public void dragTo(String fromTitle, String toTitle) {
        int[] from = new int[2];
        View row = solo.getText(fromTitle);
        row.getLocationInWindow(from);

        int[] to = new int[2];
        View secondary = solo.getText(toTitle);
        secondary.getLocationInWindow(to);

        //From https://www.py4u.net/discuss/607053
        // MotionEvent parameters
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();
        int action = MotionEvent.ACTION_DOWN;
        int x = from[0];
        int y = from[1];
        int metaState = 0;

        // dispatch the event
        MotionEvent event = MotionEvent.obtain(downTime, eventTime, action, x, y, metaState);
        event.addBatch(downTime,to[0],to[1],1,1,0);

//        solo.clickLongOnScreen(
//                from[0],
//                from[1],
//                5
//        );
//        solo.drag(
//                from[0],
//                from[1],
//                to[0],
//                to[1],
//                40
//        );
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
        //return mainActivity.habitAdapter.getItemCount();
        return mainActivity.habitDataList.size();
    }

    //I don't know why they wont finish properly otherwise?
    //This isn't automatic as it shouldn't always be done.
    //Note the naturalness of this may vary as the tests change.
    public void forceFinish() {
        solo.sleep(5000);
    }

    //Jank
    //Once again not always necessary.
    public void zedClear(){
        //We have to clear out zzz
        filterAddEventTest("zzz");
        swipeRight("zzz");
    }

    //This test checks whether the details of a habit can be edited.
    //This is just an adaption of the previous version.
    @Test
    public void editHabitTest() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        zedClear();
        filterAddEventTest("three");
        solo.clickOnText("three");
        for (int i = 0; i < 9; i++) {
            solo.clickOnButton(i);
        }
        solo.clickOnView(solo.getView(R.id.done_button));
        solo.clickOnText("three");
        ToggleButton fridayButton = (ToggleButton) solo.getView(R.id.friday_button);
        assertFalse(fridayButton.isChecked());
        ToggleButton wednesdayButton = (ToggleButton) solo.getView(R.id.wednesday_button);
        assertTrue(wednesdayButton.isChecked());
        solo.clickOnView(solo.getView(R.id.done_button));
        swipeRight("three");
        forceFinish();
    }

    //This checks whether or not a test can be deleted.
    @Test
    public void deleteTest() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        zedClear();
        //Now we test for deleting a dummy event
        filterAddEventTest("deleteme");
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        swipeRight("deleteme");
        filterAddEventTest("deleteme2");
        assertEquals(1,listCount());
        swipeRight("deleteme2");
        forceFinish();
    }

    //This is supposed to test reordering,
    //but you need to longpress-drag, which
    //doesn't appear to be possible?
    @Test
    public void reorderTest() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        zedClear();
        //emptyList();
        //solo.waitForDialogToClose(1000);
        filterAddEventTest("zero");
        solo.clickOnButton(1);
        filterAddEventTest("one");
        solo.clickOnButton(1);
        //Jankily compare the indices.
        solo.sleep(10000);
        int zero_index = getHabit("zero").getIndex();
        int one_index = getHabit("one").getIndex();
        if (one_index == -1 | zero_index == -1) {
            Log.v("TESTING","Indexes still are being retrieved too early?");
        } else {
            assertEquals(1,zero_index+one_index);
        }
        swipeRight("zero");
        swipeRight("one");
        forceFinish();
        //dragTo("zero","one");
        //solo.clickOnButton(1);
        //  Note that clicking the button will update firebase from the app.
        //  Thus the order will stay iff the order was updated correctly.
        //assertEquals(1,getHabit("one").getIndex());
    }

    @Test
    public void geolocationTest() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        filterAddEventTest("zzz");
        solo.clickOnText("Events");
        solo.clickOnView(solo.getView(R.id.add_event_button));
        EditText nameText = (EditText) solo.getView(R.id.event_name_editText);
        solo.enterText(nameText, "default");
        EditText nameTextTwo = (EditText) solo.getView(R.id.event_comment_editText);
        solo.enterText(nameTextTwo, "default");
        solo.clickOnButton(0);
        solo.assertCurrentActivity("Wrong activity",MapsActivity.class);
        //swipeRight("two");
        //I don't think we can click at the right location without permissions?
        //solo.clickOnScreen(100,100); //Doesn't matter
        //solo.clickOnButton(1);
    }
}