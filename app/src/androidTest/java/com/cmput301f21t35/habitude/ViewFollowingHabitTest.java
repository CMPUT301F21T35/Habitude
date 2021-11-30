package com.cmput301f21t35.habitude;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.Button;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ViewFollowingHabitTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<FollowingActivity> rule = new ActivityTestRule<FollowingActivity>(FollowingActivity.class,true,true);

    /**
     * Runs before all tests and creates solo instance.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

    }

    /**
     * Gets the Activity
     *
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    /**
     * Check if we can see other user's habits
     * using this account to log in, jingsheng@email.com password:password
     */
    @Test
    public void checkCreate(){
        solo.assertCurrentActivity("Wrong Activity", FollowingActivity.class);

        solo.clickOnText("jingshengtest@email.com");
        assertTrue(solo.searchText("Hiking"));


    }
}