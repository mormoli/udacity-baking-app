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

import com.example.udacity.udacity_baking_app.model.TheIngredients;
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
        //Context context;
        private ArrayList<TheIngredients> ingredientsList;
        //private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";
        private final String TAG = ListRemoteViewsFactory.class.getSimpleName();
        //private int appWidgetId;

        ListRemoteViewsFactory(Context applicationContext, Intent intent) {
            //this.context = applicationContext;
            //this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            this.ingredientsList = intent.getParcelableArrayListExtra("ingredients");
        }

        @Override
        public void onCreate() {
            // In onCreate() you set up any connections / cursors to your data source. Heavy lifting
            //recipeList = new ArrayList<>();
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
        }

        //Called when the last RemoteViewsAdapter that is associated with this factory is unbound.
        @Override
        public void onDestroy() {
            //recipeList.clear();
        }
        @SuppressWarnings("ConstantConditions")
        @Override
        public int getCount() {
            Log.d(TAG, "getCount called : " + ingredientsList.size());
            if (ingredientsList == null || ingredientsList.size() < 0) return 0;
            return ingredientsList.size();
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

            RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_row_item);
            String row = (position+1) + ". " + ingredientsList.get(position).getIngredient() + " ( " + ingredientsList.get(position).getQuantity() + " "
                    + ingredientsList.get(position).getMeasure();
            views.setTextViewText(R.id.ingredients_list_text_tv, row);
            Intent fillInIntent = new Intent();
            views.setOnClickFillInIntent(R.id.ingredients_list_text_tv, fillInIntent);
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
