package nano.yallam.bakingapp.service;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

import nano.yallam.bakingapp.R;
import nano.yallam.bakingapp.model.Recipe;


public class GridWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        ArrayList<Recipe> recipes = new ArrayList<>();
        if (intent.hasExtra(RecipesIngredientsService.EXTRA_RECIPES_LIST)) {
            recipes = (ArrayList<Recipe>) intent.getSerializableExtra(RecipesIngredientsService.EXTRA_RECIPES_LIST);
        }
        return new GridRemoteViewsFactory(this.getApplicationContext(), recipes);
    }
}

class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    Cursor mCursor;
    ArrayList<Recipe> mRecipes;

    public GridRemoteViewsFactory(Context applicationContext, ArrayList<Recipe> recipes) {
        mContext = applicationContext;
        this.mRecipes = recipes;
    }

    @Override
    public void onCreate() {

    }

    //called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {
        // Get all plant info ordered by creation time
//        Uri PLANT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLANTS).build();
//        if (mCursor != null) mCursor.close();
//        mCursor = mContext.getContentResolver().query(
//                PLANT_URI,
//                null,
//                null,
//                null,
//                PlantContract.PlantEntry.COLUMN_CREATION_TIME
//        );
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mRecipes == null) return 0;
        return mRecipes.size();
    }

    /**
     * This method acts like the onBindViewHolder method in an Adapter
     *
     * @param position The current position of the item in the GridView to be displayed
     * @return The RemoteViews object to display for the provided postion
     */
    @Override
    public RemoteViews getViewAt(int position) {
        if (mRecipes == null || mRecipes.size() == 0) return null;

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.recipes_list_item);

        views.setTextViewText(R.id.tv_recipe_name, mRecipes.get(position).getName());

//        Bundle extras = new Bundle();
//        extras.putLong(PlantDetailActivity.EXTRA_PLANT_ID, plantId);
//        Intent fillInIntent = new Intent();
//        fillInIntent.putExtras(extras);
//        views.setOnClickFillInIntent(R.id.widget_plant_image, fillInIntent);

        return views;

    }

    public void setmRecipesList(ArrayList<Recipe> recipes) {
        this.mRecipes = recipes;
        onDataSetChanged();
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1; // Treat all items in the GridView the same
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

