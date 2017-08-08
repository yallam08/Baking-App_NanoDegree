package nano.yallam.bakingapp;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

import nano.yallam.bakingapp.service.GridWidgetService;
import nano.yallam.bakingapp.service.RecipesIngredientsService;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, int recipeId) {

        // Construct the RemoteViews object
        RemoteViews views = getWidgetGridRemoteView(context, recipeId);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RecipesIngredientsService.startActionUpdateRecipes(context);
    }

    public static void updateRecipesGrid(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, int recipeId) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipeId);
        }
    }

    private static RemoteViews getWidgetGridRemoteView(Context context, int recipeId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
        Intent intent = new Intent(context, GridWidgetService.class);
        views.setRemoteAdapter(R.id.widget_grid_view, intent);

        if (recipeId == -1) {
            views.setViewVisibility(R.id.btn_widget_return, View.GONE);
            Intent ingredientsIntent = new Intent(context, RecipesIngredientsService.class);
            ingredientsIntent.setAction(RecipesIngredientsService.ACTION_GET_INGREDIENTS);
            PendingIntent appPendingIntent = PendingIntent.getService(context, 0, ingredientsIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widget_grid_view, appPendingIntent);
        } else {
            //displaying ingredients, so make return btn visible
            views.setViewVisibility(R.id.btn_widget_return, View.VISIBLE);
            Intent returnIntent = new Intent(context, RecipesIngredientsService.class);
            returnIntent.putExtra(RecipesIngredientsService.EXTRA_RECIPE_ID, -1);
            returnIntent.setAction(RecipesIngredientsService.ACTION_UPDATE_RECIPES);
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, returnIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.btn_widget_return, pendingIntent);
        }

//        views.setEmptyView(R.id.widget_grid_view, R.id.empty_view);
        return views;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        RecipesIngredientsService.startActionUpdateRecipes(context);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
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

