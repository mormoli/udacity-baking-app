package com.example.udacity.udacity_baking_app;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.udacity.udacity_baking_app.model.TheRecipe;
import com.example.udacity.udacity_baking_app.model.TheSteps;


@SuppressWarnings("ConstantConditions")
public class DetailsActivity extends AppCompatActivity implements RecipeMasterFragment.OnRecipeStepSelected{
    private static final String TAG = DetailsActivity.class.getSimpleName();
    private TheRecipe recipe;
    private TabLayout tabLayout;
    private int index;
    private boolean mTwoPane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        //setting back arrow to activity
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //check whether is tablet layout.
        mTwoPane = findViewById(R.id.step_details_fragment) != null;
        //get recipe class from an intent
        if(savedInstanceState == null){
            recipe = getIntent().getParcelableExtra("Recipe");
            //pass received object to fragment with Bundle
            Bundle bundle = new Bundle();
            bundle.putParcelable("Recipe", recipe);
            RecipeMasterFragment masterFragment = new RecipeMasterFragment();
            masterFragment.setArguments(bundle);
            masterFragment.setOnSelect(this);
            // Begin the transaction
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            //Add the contents of the container with the new fragment
            fragmentTransaction.add(R.id.master_list_fragment, masterFragment);
            //fragmentTransaction.addToBackStack(null);
            //Complete in other word commit changes added above
            fragmentTransaction.commit();
        }

        if(mTwoPane){ //if its tablet layout initialize steps detail fragment.
            Bundle bundle = new Bundle();
            TheSteps step = recipe.getTheSteps().get(0);
            //Log.d(TAG, step.toString());
            bundle.putParcelable("Step", step);
            RecipeStepsFragment stepsFragment = new RecipeStepsFragment();
            stepsFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.step_details_fragment, stepsFragment);
            fragmentTransaction.commit();
        } else {
            tabLayout = findViewById(R.id.portrait_tab_view);
            tabLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            getSupportFragmentManager().popBackStack();
            if(!mTwoPane) {//if its not tablet layout
            //check whether there is tab layout and set visibility gone (Invisible)
                if(tabLayout.getVisibility() == View.VISIBLE)
                    tabLayout.setVisibility(View.GONE);
            }
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //https://stackoverflow.com/questions/26693754/fragment-addtobackstack-and-popbackstackimmediate-not-working
    /*@Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() > 0){
            getFragmentManager().popBackStack();
        }
        super.onBackPressed();
    }*/

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("Recipe", recipe);
    }
    /**
     * method that overrides fragments on step selected method*/
    @Override
    public void OnStepSelected(int position) {

        if (!mTwoPane) { //its not tablet layout initialize steps detail fragment
            Bundle bundle = new Bundle();
            TheSteps step = recipe.getTheSteps().get(position);
            //Log.d(TAG, step.toString());
            bundle.putParcelable("Step", step);
            RecipeStepsFragment stepsFragment = new RecipeStepsFragment();
            stepsFragment.setArguments(bundle);
            // Begin the transaction
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            fragmentTransaction.replace(R.id.master_list_fragment, stepsFragment);
            fragmentTransaction.addToBackStack(stepsFragment.getClass().getSimpleName());
            // Commit the transaction
            fragmentTransaction.commit();

            index = position;
            populateTabs();
            tabLayout.setVisibility(View.VISIBLE);
        } else { // its tablet layout just set selected step to fragment to show details
            TheSteps step = recipe.getTheSteps().get(position);
            RecipeStepsFragment stepsFragment = new RecipeStepsFragment();
            stepsFragment.setCurrentStep(step);//setting new selected step
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_details_fragment, stepsFragment)
                    .commit();
        }
    }
    /**
     * Method that initializes tabs and populates view for portrait phone screen*/
    private void populateTabs(){
        final TabLayout tabLayout = findViewById(R.id.portrait_tab_view);
        //Log.d(TAG, " steps size : " + steps.size());
        for(int i = 0; i < recipe.getTheSteps().size(); i++){
            String tabText = "Step " + i;
            //creating tabs with below line code creates empty first slot then populates all
            //tabLayout.addTab(tabLayout.newTab().setText(tabText));
            //Create a new tab
            TabLayout.Tab tab = tabLayout.newTab();
            //set text to show on the tab
            tab.setText(tabText);
            //add tab to specific position in the tab layout
            tabLayout.addTab(tab, i);
        }
        tabLayout.getTabAt(index).select();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                index = tab.getPosition();//get current position of selected tab
                tabLayout.getTabAt(index).select();//select the tab
                //selected tab step
                TheSteps step = recipe.getTheSteps().get(index);
                //creating / replacing new fragment
                RecipeStepsFragment stepsFragment = new RecipeStepsFragment();
                stepsFragment.setCurrentStep(step);//setting new selected step
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.master_list_fragment, stepsFragment)
                        //.addToBackStack(null)
                        //.disallowAddToBackStack()
                        .commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /*private void handleConfigurationChanges(int position, Configuration newConfig){
        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Log.d(TAG, "portrait mode called.");
            populateTabs(position);
        }
    }*/
}
