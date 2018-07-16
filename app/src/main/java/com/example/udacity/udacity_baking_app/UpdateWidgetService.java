package com.example.udacity.udacity_baking_app;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.udacity.udacity_baking_app.model.TheIngredients;

import java.util.ArrayList;

public class UpdateWidgetService extends IntentService {
    public static final String EXTRA_ID = "com.example.udacity.udacity_baking_app.EXTRA_ID";
    public static final String UPDATE_ACTION = "android.appwidget.action.APPWIDGET_UPDATE";
    public static ArrayList<TheIngredients> ingredients;

    public UpdateWidgetService(){
        super("UpdateWidgetService");
        /**
         * If enabled is true, onStartCommand(Intent, int, int) will return Service.START_REDELIVER_INTENT,
         * so if this process dies before onHandleIntent(Intent) returns, the process will be restarted
         * and the intent redelivered. If multiple Intents have been sent, only the most recent one is
         * guaranteed to be redelivered.
         * */
        //setIntentRedelivery(true);
    }

    public static void startUpdateService(Context context, ArrayList<TheIngredients> ingredients){
        Intent intent = new Intent(context, UpdateWidgetService.class);
        intent.putParcelableArrayListExtra("ingredients", ingredients);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null){
            ingredients = intent.getParcelableArrayListExtra("ingredients");
            handleActionUpdateWidgets(ingredients);
        }
    }
    // https://stackoverflow.com/questions/3455123/programmatically-update-widget-from-activity-service-receiver/7738687#7738687
    private void handleActionUpdateWidgets(ArrayList<TheIngredients> ingredients){
        Intent intent = new Intent(this, BakingAppWidgetProvider.class);
        intent.setAction(UPDATE_ACTION);
        intent.putParcelableArrayListExtra("ingredients", ingredients);
        int[] ids = AppWidgetManager.getInstance(getApplication())
                .getAppWidgetIds(new ComponentName(getApplication(), BakingAppWidgetProvider.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
    }
}
