package nano.yallam.bakingapp.service;


import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import nano.yallam.bakingapp.IngredientsWidgetProvider;
import nano.yallam.bakingapp.R;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class RecipesIngredientsService extends IntentService {

    public static final String ACTION_UPDATE_RECIPES = "nano.yallam.bakingapp.action.update_recipes";
    public static final String ACTION_GET_INGREDIENTS = "nano.yallam.bakingapp.action.get_ingredients";
    public static final String EXTRA_RECIPE_ID = "extra_recipe_id";

    public RecipesIngredientsService() {
        super("RecipesIngredientsService");
    }

    public static void startActionUpdateRecipes(Context context) {
        Intent intent = new Intent(context, RecipesIngredientsService.class);
        intent.setAction(ACTION_UPDATE_RECIPES);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            final int recipeId = intent.getIntExtra(EXTRA_RECIPE_ID, -1);
            if (ACTION_UPDATE_RECIPES.equals(action) || ACTION_GET_INGREDIENTS.equals(action)) {
                handleActionUpdateWidgetGrid(recipeId);
            }
        }
    }

    private void handleActionUpdateWidgetGrid(int recipeId) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientsWidgetProvider.class));
        GridWidgetService.mRecipeId = recipeId;
        //Trigger data update to handle the GridView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_grid_view);
        //Now update all widgets
        IngredientsWidgetProvider.updateRecipesGrid(this, appWidgetManager, appWidgetIds, recipeId);
    }
}
