package com.example.udacity.udacity_baking_app;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.udacity.udacity_baking_app.model.TheIngredients;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidgetProvider extends AppWidgetProvider {
    private static final String TAG = BakingAppWidgetProvider.class.getSimpleName();
    private static ArrayList<TheIngredients> ingredients;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Log.d(TAG, "updateAppWidget called.");
        //https://github.com/udacity/AdvancedAndroid_MyGarden/blob/TWID.05-Solution-GridView/app/src/main/java/com/example/android/mygarden/PlantWidgetProvider.java
        RemoteViews views = getListRemoteView(context);
        //appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_view);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
    /**
     * Creates and returns the RemoteViews to be displayed in the ListView mode widget
     *
     * @param context The context
     * @return The RemoteViews for the ListView mode widget
     */
    private static RemoteViews getListRemoteView(Context context){
        Log.d(TAG, "getListRemoteView called.");
        //https://github.com/udacity/AdvancedAndroid_MyGarden/blob/TWID.05-Solution-GridView/app/src/main/java/com/example/android/mygarden/PlantWidgetProvider.java
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_view);
        // Set the ListWidgetService intent to act as the adapter
        Intent intent = new Intent(context, ListWidgetService.class);
        // Add the app widget ID to the intent extras.
        //intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        if(ingredients != null && !ingredients.isEmpty()){
            intent.putParcelableArrayListExtra("ingredients", ingredients);
        }
        views.setRemoteAdapter(R.id.list_view, intent);
        // Set the fragment detail activity intent launch when clicked
        Intent appIntent = new Intent(context, MainActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        //views.setPendingIntentTemplate(R.id.list_view, appPendingIntent);
        views.setOnClickPendingIntent(R.layout.widget_list_view, appPendingIntent);
        //Handle empty view
        views.setEmptyView(R.id.list_view, R.id.empty_view);

        return views;
    }
    //There was a problem with class ListWidgetService not calling on broadcast receive
    //manually updating setTextViewText inside onReceive method solve the problem.
    //This is called for every broadcast and before each of the above callback methods.
    @SuppressWarnings("ConstantConditions")
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(TAG, "onReceive method called. action : " + intent.getAction());
        if(intent.getAction().equals(UpdateWidgetService.UPDATE_ACTION)){
            //int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(context, BakingAppWidgetProvider.class));
            Log.d(TAG, "received : " + intent.getAction());
            if(intent.hasExtra("ingredients")) {
                ingredients = intent.getParcelableArrayListExtra("ingredients");
                Log.d(TAG, "Extra received ingredients size : "+ ingredients.size());
                //RemoteViews views = getListRemoteView(context);
                // Construct the RemoteViews object
                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_row_item);
                ComponentName widget = new ComponentName(context, BakingAppWidgetProvider.class);
                StringBuilder builder = new StringBuilder();
                for (int i=0; i < ingredients.size(); i++){
                    builder.append(i + 1).append(". ").append(ingredients.get(i).getIngredient()).append(" ( ").append(ingredients.get(i).getQuantity()).append(" ").append(ingredients.get(i).getMeasure()).append(") \n");
                }
                String result = builder.toString();
                views.setTextViewText(R.id.ingredients_list_text_tv, result);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                appWidgetManager.updateAppWidget(widget, views);
                //appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_view);
                //BakingAppWidgetProvider.postReceiveUpdate(context, appWidgetManager, appWidgetIds);
                //onUpdate(context, appWidgetManager, appWidgetIds);
            }
        }
    }

    /*public static void postReceiveUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
            //appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_view);
        }
    }*/

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "onUpdate method called.");
        // There may be multiple widgets active, so update all of them
        /*for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
            //appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_view);
        }*/
        //super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

