package com.cmput301f21t35.habitude;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

import android.app.Activity;
import android.view.View;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;


@RunWith(AndroidJUnit4.class)
public class acceptOrDenyTest {
    private Solo solo;
    @Rule
    public ActivityTestRule<FollowingActivity> rule = new ActivityTestRule<>(FollowingActivity.class,true,true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    /**
     *please making sure when you are running this test, you have log in with
     * cyao1@ualberta and password: password, so you can do the testing properly.
     * testing the app with click followers to accept the pendingfollowersrequest, and
     * check whether it is add successfully into followers
     */
    @Test
    public void acceptOrDenyTest(){
        //make sure on the right activity
        solo.assertCurrentActivity("Wrong Activity", FollowingActivity.class);
//        View requestButton = solo.getCurrentActivity().findViewById(R.id.)
//        solo.clickOnActionBarItem(2);
//        View requestButton = solo.getText(
//                "REQUESTS");
//        solo.clickOnView(requestButton);
////        solo.clickOnMenuItem("REQUESTS");
//
//        solo.clickOnButton("CONFIRM");
//        assertFalse(solo.searchText("123@email.com"));

//
        View acceptButton = solo.getCurrentActivity().findViewById(R.id.accept);
        View declineButton = solo.getCurrentActivity().findViewById(R.id.decline);
//        View backButton = solo.getCurrentActivity().findViewById(R.id.back);
//
//        solo.clickOnView(acceptButton);
//        assertTrue(solo.searchText("123@email.com"));
//        solo.clickOnButton("FOLLOWERS");
//        assertFalse(solo.searchText("123@email.com"));
//
//        solo.clickOnView(declineButton);
//        assertFalse(solo.searchText("123@email.com"));


    }
}
