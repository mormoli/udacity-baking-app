package com.example.udacity.udacity_baking_app;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidgetProvider extends AppWidgetProvider {
    public static final String EXTRA_ID = "com.example.udacity.udacity_baking_app.EXTRA_ID";
    public static final String UPDATE_ACTION = "com.example.udacity.udacity_baking_app.APPWIDGET_UPDATE";
    private static final String TAG = BakingAppWidgetProvider.class.getSimpleName();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        /*CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget_provider);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        // Create an Intent to launch MainActivity when clicked
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        //Widgets allow click handlers to only launch pending intents !
        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);*/
        Log.d(TAG, "updateAppWidget called.");
        RemoteViews views = getListRemoteView(context, appWidgetId);
        //appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_view);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
    /**
     * Creates and returns the RemoteViews to be displayed in the ListView mode widget
     *
     * @param context The context
     * @return The RemoteViews for the ListView mode widget
     */
    private static RemoteViews getListRemoteView(Context context, int appWidgetId){
        Log.d(TAG, "getListRemoteView called.");
        //https://github.com/udacity/AdvancedAndroid_MyGarden/blob/TWID.05-Solution-GridView/app/src/main/java/com/example/android/mygarden/PlantWidgetProvider.java
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_view);
        // Set the ListVidgetService intent to act as the adapter
        Intent intent = new Intent(context, ListWidgetService.class);
        // Add the app widget ID to the intent extras.
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        views.setRemoteAdapter(R.id.list_view, intent);
        // Set the fragment detail activity intent launch when clicked
        Intent appIntent = new Intent(context, MainActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.list_view, appPendingIntent);
        //Handle empty view
        views.setEmptyView(R.id.list_view, R.id.empty_view);

        return views;
    }
    /*
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive method called.");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        if(intent.getAction().equals(UPDATE_ACTION)){
            int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(context, BakingAppWidgetProvider.class));
            Log.d(TAG, "received : " + intent.getAction());
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.list_view);
        }
        super.onReceive(context, intent);
    }*/

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d(TAG, "onUpdate method called.");
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        /*
        for (int i = 0; i < appWidgetIds.length; i++) {
            //setup intent that starts the ListWidgetService - provide views for this collection
            Intent intent = new Intent(context, ListWidgetService.class);
            //Add the app widget ID to the intent extras
            intent.putExtra(BakingAppWidgetProvider.EXTRA_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_view);
            views.setRemoteAdapter(appWidgetIds[i], R.id.list_view, intent);
            //trigger list view item click
            Intent startActivityIntent = new Intent(context, MainActivity.class);
            PendingIntent startActivityPendingIntent = PendingIntent.getActivity(
                    context, 0, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT
            );
            views.setPendingIntentTemplate(R.id.list_view, startActivityPendingIntent);
            //The empty view is displayed when there is no item to show
            //It should be in the same layout used to instantiate the RemoteViews object.
            views.setEmptyView(R.id.list_view, R.id.empty_view);
            //updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);*/
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

