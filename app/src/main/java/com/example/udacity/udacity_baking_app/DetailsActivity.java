package com.example.udacity.udacity_baking_app;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.udacity.udacity_baking_app.model.TheRecipe;
import com.example.udacity.udacity_baking_app.model.TheSteps;
import com.example.udacity.udacity_baking_app.utils.RecipeMasterAdapter;

@SuppressWarnings("ConstantConditions")
public class DetailsActivity extends AppCompatActivity implements RecipeMasterFragment.OnRecipeStepSelected{
    private static final String TAG = DetailsActivity.class.getSimpleName();
    private TheRecipe recipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        //setting back arrow to activity
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    @Override
    public void OnStepSelected(int position) {
        //step list detail depending on step position
        //open or replace fragment with detail fragment
        //video url retun null condition check !!!
        // in order to not throw exception !
        Bundle bundle = new Bundle();
        TheSteps step = recipe.getTheSteps().get(position);
        Log.d(TAG, step.toString());
        bundle.putParcelable("Step", step);
        RecipeStepsFragment stepsFragment = new RecipeStepsFragment();
        stepsFragment.setArguments(bundle);
        // Begin the transaction
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        fragmentTransaction.replace(R.id.master_list_fragment, stepsFragment);
        fragmentTransaction.addToBackStack(null);
        // Commit the transaction
        fragmentTransaction.commit();
    }
}
