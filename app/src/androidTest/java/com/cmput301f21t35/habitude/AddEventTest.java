package com.cmput301f21t35.habitude;

import android.app.Activity;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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
public class AddEventTest {
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
     * Try and add activity without entering anything
     * Then try and add normally
     * also checks that comment is not more than 20 characters
     *
     * BEFORE RUNNING TESTS ENSURE THAT THERE IS A HABIT CALLED "Hiking"
     */
    @Test
    public void addEventTest() {
        // make sure you are on right activity
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnText("Hiking"); // enter hiking event
        // make sure you are on the right activity
        solo.assertCurrentActivity("Wrong Activity", EditHabitActivity.class);
        // open events page
        solo.clickOnButton("Events");
        // make sure you are on the right activity
        solo.assertCurrentActivity("Wrong Activity", EventListActivity.class);
        View floatingActionButton = (View) rule.getActivity().findViewById(R.id.add_event_button);
        solo.clickOnView(floatingActionButton);
        // click on the add event button
        solo.getCurrentActivity().getFragmentManager().findFragmentByTag("ADD EVENT");
        // first verify app doesn't crash when nothing is inputted
        solo.clickOnText("OK");
        // then try and actually add values
        solo.getCurrentActivity().getFragmentManager().findFragmentByTag("ADD EVENT");
        EditText nameText = (EditText) solo.getView(R.id.event_name_editText);
        EditText commentText = (EditText) solo.getView(R.id.event_comment_editText);
        // input into fields
        solo.enterText(nameText, "test name");
        solo.enterText(commentText, "this string should be cut off somewhere");
        String comment = commentText.getText().toString();
        // check that length gets cut off
        assertEquals(20, comment.length());
        solo.clickOnText("OK");
        solo.waitForText("test name", 1, 2000);
        // get list of events
        EventListActivity activity = (EventListActivity) solo.getCurrentActivity();
        final ListView eventsList = activity.eventList;
        Event event = (Event) eventsList.getItemAtPosition(0); // get name from list
        assertEquals("test name", event.getEventName());
    }
}
