package com.cmput301f21t35.habitude;

import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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
     * This test performs 6 checks:
     * 1. send request normally
     * 2. send request to someone that does not exist
     * 3. send request to someone that you are already following
     * 4. send request to someone you have already sent a request to
     * 5. send request to yourself
     * 6. don't enter anything
     * FOR THIS TEST TO WORK MAKE SURE YOU ARE ON send@email.com, PASSWORD: password
     * ALSO MAKE SURE recieve@email.com HAS NO REQUEST FROM send@email.com, PASSWORD:password
     */
    @Test
    public void sendRequestTest() {
        // make sure you are on right activity
        solo.assertCurrentActivity("Wrong Activity", FollowingActivity.class);
        View floatingActionButton = solo.getCurrentActivity().findViewById(R.id.follow_New_Button);
        // first try and send a request to recieve@email.com (this should work)
        solo.clickOnView(floatingActionButton);
        EditText editText = (EditText) solo.getView(R.id.user_email_editText);
        solo.enterText(editText, "recieve@email.com");
        solo.clickOnText("OK");
        solo.waitForText("Request sent!"); // wait for toast message
        // next try and send a request to someone that does not exist
        solo.clickOnView(floatingActionButton);
        editText = (EditText) solo.getView(R.id.user_email_editText);
        solo.enterText(editText, "noone");
        solo.clickOnText("OK");
        solo.waitForText("User does not exist!"); // wait for toast message
        // try and send to someone you are already following
        solo.clickOnView(floatingActionButton);
        editText = (EditText) solo.getView(R.id.user_email_editText);
        solo.enterText(editText, "followed@email.com");
        solo.clickOnText("OK");
        solo.waitForText("You are already following!"); // wait for toast message
        // try and send to someone you are already requesting
        solo.clickOnView(floatingActionButton);
        editText = (EditText) solo.getView(R.id.user_email_editText);
        solo.enterText(editText, "receive@email.com");
        solo.clickOnText("OK");
        solo.waitForText("You have already requested!"); // wait for toast message
        // try and send to someone you are already requesting
        solo.clickOnView(floatingActionButton);
        editText = (EditText) solo.getView(R.id.user_email_editText);
        solo.enterText(editText, "receive@email.com");
        solo.clickOnText("OK");
        solo.waitForText("You have already requested!"); // wait for toast message
        // try and send to yourself
        solo.clickOnView(floatingActionButton);
        editText = (EditText) solo.getView(R.id.user_email_editText);
        solo.enterText(editText, "send@email.com");
        solo.clickOnText("OK");
        solo.waitForText("Cannot follow Yourself!"); // wait for toast message
        // try and enter nothing
        solo.clickOnView(floatingActionButton);
        solo.clickOnText("OK");
        solo.waitForText("Some fields are blank!"); // wait for toast message
    }
}
