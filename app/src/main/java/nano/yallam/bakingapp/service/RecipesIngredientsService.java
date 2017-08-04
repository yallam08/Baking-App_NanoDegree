package nano.yallam.bakingapp.service;


import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import nano.yallam.bakingapp.IngredientsWidgetProvider;
import nano.yallam.bakingapp.R;
import nano.yallam.bakingapp.api.BackendRequest;
import nano.yallam.bakingapp.api.EndPoints;
import nano.yallam.bakingapp.model.Recipe;
import retrofit2.Call;
import retrofit2.Response;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class RecipesIngredientsService extends IntentService {

    public static final String ACTION_UPDATE_RECIPES = "nano.yallam.bakingapp.action.update_recipes";
    public static final String ACTION_GET_INGREDIENTS = "nano.yallam.bakingapp.action.get_ingredients";
    public static final String EXTRA_RECIPES_LIST = "extra_recipes_list";

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
            if (ACTION_UPDATE_RECIPES.equals(action)) {
                handleActionUpdateRecipes();
            } else if (ACTION_GET_INGREDIENTS.equals(action)) {
//                final long recipe = intent.getLongExtra(EXTRA_PLANT_ID,
//                        PlantContract.INVALID_PLANT_ID);
//                handleActionGetIngredients(recipe);
            }
        }
    }

//    private void handleActionGetIngredients(long plantId) {
//        Uri SINGLE_PLANT_URI = ContentUris.withAppendedId(
//                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLANTS).build(), plantId);
//        ContentValues contentValues = new ContentValues();
//        long timeNow = System.currentTimeMillis();
//        contentValues.put(PlantContract.PlantEntry.COLUMN_LAST_WATERED_TIME, timeNow);
//        // Update only if that plant is still alive
//        getContentResolver().update(
//                SINGLE_PLANT_URI,
//                contentValues,
//                PlantContract.PlantEntry.COLUMN_LAST_WATERED_TIME + ">?",
//                new String[]{String.valueOf(timeNow - PlantUtils.MAX_AGE_WITHOUT_WATER)});
//        // Always update widgets after watering plants
//        startActionUpdatePlantWidgets(this);
//    }

    private void handleActionUpdateRecipes() {
        final ArrayList<Recipe> recipesList = new ArrayList<>();

        EndPoints request = BackendRequest.createRequest();
        Call<ArrayList<Recipe>> recipesCall = request.getRecipes();
        try {
            Response<ArrayList<Recipe>> response = recipesCall.execute();
            if (response != null && response.body() != null) {
                recipesList.addAll(response.body());
            }
        } catch (IOException e) {
            Log.d("Error", e.getMessage());
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientsWidgetProvider.class));
        //Trigger data update to handle the GridView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_recipes_grid_view);
        //Now update all widgets
        IngredientsWidgetProvider.updateRecipesGrid(this, appWidgetManager, appWidgetIds);
    }
}
