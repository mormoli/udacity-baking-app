package com.example.udacity.udacity_baking_app;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.udacity.udacity_baking_app.model.TheIngredients;
import com.example.udacity.udacity_baking_app.model.TheRecipe;
import com.example.udacity.udacity_baking_app.utils.RecipesListAdapter;
import com.example.udacity.udacity_baking_app.utils.TheRecipesInterface;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity{
    @BindView(R.id.recipes_list_view)
    RecyclerView recyclerView;
    private RecipesListAdapter recipesListAdapter;
    private ArrayList<TheRecipe> recipeList = new ArrayList<>();
    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";
    private static final String TAG = MainActivity.class.getSimpleName();
    private Parcelable recyclerViewState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //binding views to this activity with ButterKnife library
        ButterKnife.bind(this);
        //if savedInstanceState null or recipe list empty retrieve recipe list from network
        if(savedInstanceState == null) getRecipes();
        else recipeList = savedInstanceState.getParcelableArrayList("Recipes");
        //defining recycler view layout , adapter etc
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        //recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recipesListAdapter = new RecipesListAdapter(recipeList);
        recyclerView.setAdapter(recipesListAdapter);
        recipesListAdapter.setOnClick(new RecipesListAdapter.OnItemClicked() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "item: " + position);
                //get the recipe list
                ArrayList<TheIngredients> ingList = recipeList.get(position).getTheIngredients();
                //build string ingredient list to show on widget
                /*StringBuilder builder = new StringBuilder();
                for (int i=0; i < ingList.size(); i++){
                    builder.append(ingList.get(i).getIngredient() + " ( " + ingList.get(i).getQuantity() + " "
                            + ingList.get(i).getMeasure() + ") \n");
                }
                String result = builder.toString();*/
                //start widget update service to update widget
                UpdateWidgetService.startUpdateService(view.getContext(), ingList);
                //start detailed list activity
                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                intent.putExtra("Recipe", recipeList.get(position));
                startActivity(intent);
            }
        });
        /*ListViewFragment listViewFragment = new ListViewFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.list_view_fragment, listViewFragment)
                .commit();*/
    }

    /**
     * Method that retrieves recipe list json from source and saves values in an array list
     *
     * */
    public void getRecipes(){
        if(recipeList == null) recipeList = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        TheRecipesInterface theRecipesInterface = retrofit.create(TheRecipesInterface.class);
        Call<ArrayList<TheRecipe>> call = theRecipesInterface.getRecipes();
        call.enqueue(new Callback<ArrayList<TheRecipe>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<TheRecipe>> call, @NonNull Response<ArrayList<TheRecipe>> response) {
                recipeList.addAll(response.body());
                Log.d(TAG, "Recipe list size: " + recipeList.size());
                recipesListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<TheRecipe>> call, @NonNull Throwable t) {
                Log.d(TAG, t.toString());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(recyclerViewState != null)
            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null) {
            //calling saved state in on create.
            //recipeList = savedInstanceState.getParcelableArrayList("Recipes");
            recyclerViewState = savedInstanceState.getParcelable("scroll_position");
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("Recipes", recipeList);
        if(recyclerView != null) {
            recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
            outState.putParcelable("scroll_position", recyclerViewState);
        }
    }
}
