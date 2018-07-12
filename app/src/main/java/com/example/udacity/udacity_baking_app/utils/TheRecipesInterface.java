package com.example.udacity.udacity_baking_app.utils;

import com.example.udacity.udacity_baking_app.model.TheRecipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TheRecipesInterface {
    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<ArrayList<TheRecipe>> getRecipes();
}
