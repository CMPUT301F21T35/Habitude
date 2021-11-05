package com.cmput301f21t35.habitude;

import androidx.fragment.app.Fragment;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ViewEventTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void viewEventTest() {
        // make sure you are on right activity
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnText("Hiking"); // enter hiking event
        // make sure you are on the right activity
        solo.assertCurrentActivity("Wrong Activity", EditHabitActivity.class);
        // open events page
        solo.clickOnButton("Events");
        // make sure you are on the right activity
        solo.assertCurrentActivity("Wrong Activity", EventListActivity.class);
        solo.clickInList(0);
        solo.assertCurrentActivity("Wrong Activity", HabitEventActivity.class);


    }
}

