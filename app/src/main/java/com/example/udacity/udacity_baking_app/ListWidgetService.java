package com.example.udacity.udacity_baking_app;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.udacity.udacity_baking_app.model.TheRecipe;
import com.example.udacity.udacity_baking_app.model.TheSteps;
import com.example.udacity.udacity_baking_app.utils.TheRecipesInterface;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        Context context;
        private ArrayList<TheRecipe> recipeList;
        private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";
        private final String TAG = ListRemoteViewsFactory.class.getSimpleName();
        private int appWidgetId;

        ListRemoteViewsFactory(Context applicationContext, Intent intent) {
            this.context = applicationContext;
            this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
            // In onCreate() you set up any connections / cursors to your data source. Heavy lifting
            recipeList = new ArrayList<>();
        }
        //called on start and when notifyAppWidgetViewDataChanged is called

        /**
         * Called when notifyDataSetChanged() is triggered on the remote adapter.
         * This allows a RemoteViewsFactory to respond to data changes by updating any internal references.
         * Note: expensive tasks can be safely performed synchronously within this method.
         * In the interim, the old data will be displayed within the widget.
         */
        @Override
        public void onDataSetChanged() {
            Log.d(TAG, "onDataSetChanged method called.");
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
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<TheRecipe>> call, @NonNull Throwable t) {
                    Log.d(TAG, t.toString());
                }
            });

        }

        //Called when the last RemoteViewsAdapter that is associated with this factory is unbound.
        @Override
        public void onDestroy() {
            recipeList.clear();
        }

        @Override
        public int getCount() {
            Log.d(TAG, "getCount called : " + recipeList.size());
            if (recipeList == null || recipeList.size() == 0) return 4;
            return recipeList.size();
        }

        /**
         * This method acts like the onBindViewHolder method in an Adapter
         *
         * @param position The current position of the item in the ListView to be displayed
         * @return The RemoteViews object to display for the provided position
         */
        @Override
        public RemoteViews getViewAt(int position) {
            Log.d(TAG, "getViewAt method called.");
            if (recipeList == null || recipeList.size() == 0) {
                Log.d(TAG, "recipe list is Null !!");
                return null;
            }


            final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_row_item);
            //setting image
            /*if (recipeList.get(position).getImageURL() != null) {
                Picasso.get()
                        .load(recipeList.get(position).getImageURL())
                        .placeholder(R.drawable.ic_chocolate_heart)
                        .into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                views.setImageViewBitmap(R.id.list_image_tv, bitmap);
                            }

                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                                //do something when loading failed
                                Log.d(TAG, "Bitmap loading failed!");
                            }


                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {
                                //do something while loading
                            }
                        });
            }*/
            //recipe name
            String recipeName = recipeList.get(position).getName();
            views.setTextViewText(R.id.recipe_header_tv, recipeName);
            views.setViewVisibility(R.id.recipe_header_tv, View.VISIBLE);
            Log.d(TAG, recipeName);
            //short description
            ArrayList<TheSteps> theSteps = recipeList.get(position).getTheSteps();
            String shortDescription = theSteps.get(0).getShortDescription();
            views.setTextViewText(R.id.recipe_sort_description_tv, shortDescription);
            views.setViewVisibility(R.id.recipe_sort_description_tv, View.VISIBLE);
            Log.d(TAG, shortDescription);
            //servings
            String servings = "Servings: " + recipeList.get(position).getServings();
            views.setTextViewText(R.id.servings_tv, servings);
            views.setViewVisibility(R.id.servings_tv, View.VISIBLE);
            Log.d(TAG, servings);
            // Fill in the onClick PendingIntent Template using the specific Id for each item individually
            //Bundle extras = new Bundle();
            //extras.putInt(BakingAppWidgetProvider.EXTRA_ID, position);
            Intent fillInIntent = new Intent();
            //fillInIntent.putExtras(extras);
            views.setOnClickFillInIntent(R.id.list_image_tv, fillInIntent);
            views.setOnClickFillInIntent(R.id.recipe_header_tv, fillInIntent);

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
