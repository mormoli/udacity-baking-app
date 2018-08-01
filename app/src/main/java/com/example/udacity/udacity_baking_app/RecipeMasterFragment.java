package com.example.udacity.udacity_baking_app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.udacity.udacity_baking_app.model.TheIngredients;
import com.example.udacity.udacity_baking_app.model.TheRecipe;
import com.example.udacity.udacity_baking_app.model.TheSteps;
import com.example.udacity.udacity_baking_app.utils.RecipeMasterAdapter;

import java.util.ArrayList;

public class RecipeMasterFragment extends Fragment {
    private static final String TAG = RecipeMasterAdapter.class.getSimpleName();
    RecyclerView recyclerView;
    private TheRecipe recipe;
    private RecipeMasterAdapter recipeMasterAdapter;
    private OnRecipeStepSelected onRecipeStepSelected;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public RecipeMasterFragment(){}

    public static RecipeMasterFragment newInstance(){
        return new RecipeMasterFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            recipe = getArguments().getParcelable("Recipe");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_master_list, container, false);
        recyclerView = rootView.findViewById(R.id.steps_list_tv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ArrayList<TheSteps> steps = recipe.getTheSteps();
        recipeMasterAdapter = new RecipeMasterAdapter(steps);
        recyclerView.setAdapter(recipeMasterAdapter);
        return rootView;
    }
    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView headerTv = view.findViewById(R.id.ingredients_header_tv);
        TextView ingredients = view.findViewById(R.id.ingredients_list_tv);
        //setting recipe name as header
        headerTv.setText(recipe.getName());
        //setting list of ingredients in a text view
        ArrayList<TheIngredients> ingList = recipe.getTheIngredients();
        StringBuilder builder = new StringBuilder();
        for (int i=0; i < ingList.size(); i++){
            builder.append(ingList.get(i).getIngredient() + " ( " + ingList.get(i).getQuantity() + " "
                    + ingList.get(i).getMeasure() + ") \n");
        }
        String result = builder.toString();
        ingredients.setText(result);

        recipeMasterAdapter.setOnClick(new RecipeMasterAdapter.OnItemClicked() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG, "clicked: " + position);
                onRecipeStepSelected.OnStepSelected(position);
                /*if(view.getTag().equals("selected")) {
                    view.setBackgroundResource(R.color.colorAccent);
                }*/
            }
        });
    }
    // This method is called when the fragment is no longer connected to the Activity
    // Any references saved in onAttach should be nulled out here to prevent memory leaks.
    @Override
    public void onDetach() {
        super.onDetach();
        if (recipe != null) recipe = null;
    }

    public interface OnRecipeStepSelected{
        void OnStepSelected(int position);
    }

    public void setOnSelect(OnRecipeStepSelected onSelect){
        onRecipeStepSelected = onSelect;
    }
}
