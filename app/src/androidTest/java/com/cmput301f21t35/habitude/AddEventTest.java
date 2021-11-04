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
    public ActivityTestRule<EventListActivity> rule =
            new ActivityTestRule<>(EventListActivity.class, true, true);

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
     * Try and add activity
     * also checks that comment is not more than 20 characters
     */
    @Test
    public void addEventTest() {
        solo.assertCurrentActivity("Wrong Activity", EventListActivity.class);
        View floatingActionButton = rule.getActivity().findViewById(R.id.add_event_button);
        solo.clickOnView(floatingActionButton);
        solo.getCurrentActivity().getFragmentManager().findFragmentByTag("ADD EVENT");
        EditText nameText = (EditText) solo.getView(R.id.event_name_editText);
        EditText commentText = (EditText) solo.getView(R.id.event_comment_editText);
        solo.enterText(nameText, "test name");
        solo.enterText(commentText, "this string should be cut off somewhere");
        String comment = commentText.getText().toString();
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
