package com.example.udacity.udacity_baking_app;

import android.content.res.Configuration;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.udacity.udacity_baking_app.model.TheRecipe;
import com.example.udacity.udacity_baking_app.model.TheSteps;

import java.util.ArrayList;

@SuppressWarnings("ConstantConditions")
public class DetailsActivity extends AppCompatActivity implements RecipeMasterFragment.OnRecipeStepSelected{
    private static final String TAG = DetailsActivity.class.getSimpleName();
    private TheRecipe recipe;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        //setting back arrow to activity
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tabLayout = findViewById(R.id.portrait_tab_view);
        tabLayout.setVisibility(View.GONE);
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
            //Complete in other word commit changes added above
            fragmentTransaction.commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("Recipe", recipe);
    }
    /**
     * method that overrides fragments on step selected method*/
    @Override
    public void OnStepSelected(int position) {
        //step list detail depending on step position
        //open or replace fragment with detail fragment
        //video url retun null condition check !!!
        // in order to not throw exception !
        Bundle bundle = new Bundle();
        TheSteps step = recipe.getTheSteps().get(position);
        //Log.d(TAG, step.toString());
        bundle.putParcelable("Step", step);
        //bundle.putParcelable("Recipe", recipe);
        //bundle.putInt("index", position);
        RecipeStepsFragment stepsFragment = new RecipeStepsFragment();
        stepsFragment.setArguments(bundle);
        // Begin the transaction
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        fragmentTransaction.replace(R.id.master_list_fragment, stepsFragment, "STEPS_FRAGMENT");
        fragmentTransaction.addToBackStack(null);
        // Commit the transaction
        fragmentTransaction.commit();
        //handleConfigurationChanges(position, new Configuration());
        //Configuration.ORIENTATION_PORTRAIT == 1 &&
        //if(stepsFragment.isVisible() && stepsFragment != null) {
            populateTabs(position);
            tabLayout.setVisibility(View.VISIBLE);
        //} else {
            //tabLayout.setVisibility(View.GONE);
            //tabLayout.removeAllTabs();
        //}
    }
    /**
     * Method that initializes tabs and populates view for portrait phone screen*/
    private void populateTabs(final int position){
        //final ViewPager viewPager = findViewById(R.id.view_pager_tv);
        //viewPager.setVisibility(View.VISIBLE);
        //viewPager.setCurrentItem(position);
        final TabLayout tabLayout = findViewById(R.id.portrait_tab_view);
        //ArrayList<TheSteps> steps = recipe.getTheSteps();
        //Log.d(TAG, " steps size : " + steps.size());
        //tabLayout.setupWithViewPager(viewPager);
        for(int i = 0; i < recipe.getTheSteps().size(); i++){
            String tabText = "Step " + i;
            tabLayout.addTab(tabLayout.newTab().setText(tabText));
        }
        tabLayout.getTabAt(position).select();
        //TabLayout.Tab tab = tabLayout.getTabAt(position);
        //tab.select();
        /*PagerAdapter adapter = new PagerAdapter(){

            @Override
            public int getCount() {
                return tabLayout.getTabCount();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return false;
            }
        };*/
        //viewPager.setAdapter(adapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //viewPager.setCurrentItem(tab.getPosition());
                tabLayout.getTabAt(position).select();
                Bundle bundle = new Bundle();
                TheSteps step = recipe.getTheSteps().get(position);
                bundle.putParcelable("Step", step);
                //RecipeStepsFragment stepsFragment = new RecipeStepsFragment();
                Fragment fragment = getSupportFragmentManager().findFragmentByTag("STEPS_FRAGMENT");
                // Begin the transaction
                getSupportFragmentManager().beginTransaction().remove(fragment).commit();//.detach(fragment).commitNowAllowingStateLoss();
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().add(R.id.master_list_fragment, fragment, "STEPS_FRAGMENT").commit();//.attach(fragment).commitAllowingStateLoss();
                //fragmentTransaction.replace(R.id.master_list_fragment, fragment, "STEPS_FRAGMENT");
                //fragmentTransaction.addToBackStack(null);
                // Commit the transaction
                //fragmentTransaction.commit();
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
            Log.d(TAG, "portrain mode called.");
            populateTabs(position);
        }
    }*/
}
