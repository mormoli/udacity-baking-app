package com.example.udacity.udacity_baking_app;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class ActivitiesIdlingResourceTests {
    /**
     *
     * This test demonstrates Espresso Intents using the IntentsTestRule, a class that extends
     * ActivityTestRule. IntentsTestRule initializes Espresso-Intents before each test that is annotated
     * with @Test and releases it once the test is complete. The designated Activity
     * is also terminated after each test.
     *
     */
    @Rule
    public IntentsTestRule<MainActivity> mainActivityActivityTestRule = new IntentsTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;

    // Registers any resource that needs to be synchronized with Espresso before the test is run.
    @Before
    public void registerIdlingResource(){
        mIdlingResource = mainActivityActivityTestRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        //@see "https://stackoverflow.com/questions/47078005/why-is-espressos-registeridlingresources-deprecated-and-what-replaces-it"
        //Espresso.registerIdlingResources();
        IdlingRegistry.getInstance().register(mIdlingResource);
        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
        // every test run. In this case all external Intents will be blocked.
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }
    /*
    * method that tests on recycler view item click
    * sending intent to DetailsActivity class with Recipe details
    * */
    @Test
    public void clickRecyclerViewToUpdateRecipeDetails(){
        // recycler view item and clicks it.
        onView(withId(R.id.recipes_list_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // intended(Matcher<Intent> matcher) asserts the given matcher matches one and only one
        // intent sent by the application.
        intended(hasExtraWithKey("Recipe"));
    }
    // Remember to unregister resources when not needed to avoid malfunction.
    @After
    public void unregisterIdlingResource(){
        if(mIdlingResource != null)
            IdlingRegistry.getInstance().unregister(mIdlingResource);
    }
}
