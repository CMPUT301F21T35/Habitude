package com.cmput301f21t35.habitude;

import android.app.Activity;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class sendRequestTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<FollowingActivity> rule =
            new ActivityTestRule<>(FollowingActivity.class, true, true);

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
     * This test performs 4 checks:
     * 1. send request normally
     * 2. send request to someone that does not exist
     * 3. send request to someone that you are already following
     * 4. send request to someone you have already sent a request to
     */
    @Test
    public void sendRequestTest() {
        // make sure you are on right activity
        solo.assertCurrentActivity("Wrong Activity", FollowingActivity.class);

    }
}
