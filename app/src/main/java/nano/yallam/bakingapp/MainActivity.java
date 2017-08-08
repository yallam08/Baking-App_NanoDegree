package nano.yallam.bakingapp;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import nano.yallam.bakingapp.api.BackendRequest;
import nano.yallam.bakingapp.api.EndPoints;
import nano.yallam.bakingapp.model.Ingredient;
import nano.yallam.bakingapp.model.Recipe;
import nano.yallam.bakingapp.model.Step;
import retrofit2.Call;
import retrofit2.Response;

import nano.yallam.bakingapp.provider.DBContract.IngredientEntry;
import nano.yallam.bakingapp.provider.DBContract.RecipeEntry;
import nano.yallam.bakingapp.provider.DBContract.StepEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Recipe>> {

    private static final int RECIPES_LOADER_ID = 15;
    public static final String RECIPE_STEPS_LIST_KEY = "steps_list";

    @BindView(R.id.grid_view_recipes)
    GridView recipesGridView;

    @BindView(R.id.pb_loading_recipes)
    ProgressBar loadingRecipesProgressBar;

    @BindView(R.id.tv_error_loading_recipes)
    TextView errorLoadingRecipesTextView;

    RecipesListAdapter recipesListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        recipesListAdapter = new RecipesListAdapter(this, null);
        recipesGridView.setAdapter(recipesListAdapter);
        recipesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recipe recipe = recipesListAdapter.getRecipes().get(position);
                Intent stepsIntent = new Intent(MainActivity.this, StepsDetailsActivity.class);
                stepsIntent.putExtra(RECIPE_STEPS_LIST_KEY, recipe.getSteps());
                startActivity(stepsIntent);
            }
        });


        getSupportLoaderManager().initLoader(RECIPES_LOADER_ID, null, this);
    }

    @Override
    public Loader<ArrayList<Recipe>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<Recipe>>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Override
            public ArrayList<Recipe> loadInBackground() {
                final ArrayList<Recipe> returnRecipes = new ArrayList<>();

                EndPoints request = BackendRequest.createRequest();
                Call<ArrayList<Recipe>> recipesCall = request.getRecipes();
                try {
                    Response<ArrayList<Recipe>> response = recipesCall.execute();
                    if (response != null && response.body() != null) {
                        returnRecipes.addAll(response.body());

                        getContentResolver().delete(RecipeEntry.CONTENT_URI, null, null);
                        getContentResolver().delete(StepEntry.CONTENT_URI, null, null);
                        getContentResolver().delete(IngredientEntry.CONTENT_URI, null, null);

                        for (Recipe recipe: returnRecipes) {
                            ContentValues recipeContentValues = new ContentValues();
                            recipeContentValues.put(RecipeEntry.COLUMN_RECIPE_ID, recipe.getId());
                            recipeContentValues.put(RecipeEntry.COLUMN_NAME, recipe.getName());
                            recipeContentValues.put(RecipeEntry.COLUMN_SERVINGS, recipe.getServings());
                            recipeContentValues.put(RecipeEntry.COLUMN_IMAGE, recipe.getImage());

                            getContentResolver().insert(RecipeEntry.CONTENT_URI, recipeContentValues);

                            for (Step step: recipe.getSteps()) {
                                ContentValues stepContentValues = new ContentValues();
                                stepContentValues.put(StepEntry.COLUMN_STEP_ID, step.getId());
                                stepContentValues.put(StepEntry.COLUMN_RECIPE_ID, recipe.getId());
                                stepContentValues.put(StepEntry.COLUMN_SHORT_DESCRIPTION, step.getShortDescription());
                                stepContentValues.put(StepEntry.COLUMN_DESCRIPTION, step.getDescription());
                                stepContentValues.put(StepEntry.COLUMN_VIDEO_URL, step.getVideoURL());
                                stepContentValues.put(StepEntry.COLUMN_THUMB_URL, step.getThumbnailURL());

                                getContentResolver().insert(StepEntry.CONTENT_URI, stepContentValues);
                            }

                            for (Ingredient ingredient: recipe.getIngredients()) {
                                ContentValues ingredientContentValues = new ContentValues();
                                ingredientContentValues.put(IngredientEntry.COLUMN_RECIPE_ID, recipe.getId());
                                ingredientContentValues.put(IngredientEntry.COLUMN_INGREDIENT, ingredient.getIngredient());
                                ingredientContentValues.put(IngredientEntry.COLUMN_MEASURE, ingredient.getMeasure());
                                ingredientContentValues.put(IngredientEntry.COLUMN_QUANTITY, ingredient.getQuantity());

                                getContentResolver().insert(IngredientEntry.CONTENT_URI, ingredientContentValues);
                            }
                        }
                    }
                } catch (IOException e) {
                    Log.d("Error", e.getMessage());
                }

                return returnRecipes;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Recipe>> loader, ArrayList<Recipe> recipes) {
        if (recipes.size() > 0) {
            showRecipes();
        } else {
            showErrorLoadingRecipes();
        }
        recipesListAdapter.setRecipes(recipes);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Recipe>> loader) {

    }

    private void showRecipes() {
        recipesGridView.setVisibility(View.VISIBLE);
        errorLoadingRecipesTextView.setVisibility(View.INVISIBLE);
        loadingRecipesProgressBar.setVisibility(View.INVISIBLE);
    }

    private void showErrorLoadingRecipes() {
        recipesGridView.setVisibility(View.INVISIBLE);
        errorLoadingRecipesTextView.setVisibility(View.VISIBLE);
        loadingRecipesProgressBar.setVisibility(View.INVISIBLE);
    }
}
