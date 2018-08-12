package com.example.udacity.udacity_baking_app;

import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import android.support.test.espresso.contrib.RecyclerViewActions;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * This test demos a user clicking on a Recycler View item in MainActivity which opens up the
 * corresponding DetailsActivity.
 *
 * This test does not utilize Idling Resources yet. If idling is set in the MainActivity,
 * then this test will fail. See the IdlingResourcesTest for an identical test that
 * takes into account Idling Resources.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityScreenTest {
    private static final String RECIPE_NAME = "Nutella Pie";

    /**
     * The ActivityTestRule is a rule provided by Android used for functional testing of a single
     * activity. The activity that will be tested will be launched before each test that's annotated
     * with @Test and before methods annotated with @Before. The activity will be terminated after
     * the test and methods annotated with @After are complete. This rule allows you to directly
     * access the activity during the test.
     */
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    /**
     * Clicks on a RecyclerView item and checks it opens up the DetailsActivity with the correct details.
     */
    @Test
    public void clickRecyclerViewToOpenActivity(){
        // recyclerview item and clicks it.
        onView(withId(R.id.recipes_list_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Checks that the DetailsActivity opens with the correct recipe name displayed
        onView(withId(R.id.ingredients_header_tv)).check(matches(withText(RECIPE_NAME)));
    }
}
