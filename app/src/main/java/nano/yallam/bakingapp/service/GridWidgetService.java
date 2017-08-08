package nano.yallam.bakingapp.service;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;


import nano.yallam.bakingapp.R;
import nano.yallam.bakingapp.provider.DBContract;

import static nano.yallam.bakingapp.service.GridWidgetService.mRecipeId;


public class GridWidgetService extends RemoteViewsService {
    public static int mRecipeId = -1;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext());
    }
}

class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private Cursor mCursor;


    public GridRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {

    }

    //called on start and when notifyAppWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {
        if (mCursor != null) mCursor.close();
        if (mRecipeId >= 0) {
            mCursor = mContext.getContentResolver().query(
                    DBContract.IngredientEntry.CONTENT_URI,
                    null,
                    DBContract.IngredientEntry.COLUMN_RECIPE_ID + " = ?",
                    new String[] {String.valueOf(mRecipeId)},
                    null
            );
        } else {
            mCursor = mContext.getContentResolver().query(
                    DBContract.RecipeEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    /**
     * This method acts like the onBindViewHolder method in an Adapter
     *
     * @param position The current position of the item in the GridView to be displayed
     * @return The RemoteViews object to display for the provided postion
     */
    @Override
    public RemoteViews getViewAt(int position) {
        if (mCursor == null || mCursor.getCount() == 0 || position >= mCursor.getCount()) {
            return null;
        }

        mCursor.moveToPosition(position);

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
        if (mRecipeId >= 0) {
            String ingredientName = mCursor.getString(mCursor.getColumnIndex(DBContract.IngredientEntry.COLUMN_INGREDIENT));
            views.setTextViewText(R.id.tv_widget_item_name, ingredientName);
        } else {
            String recipeName = mCursor.getString(mCursor.getColumnIndex(DBContract.RecipeEntry.COLUMN_NAME));
            views.setTextViewText(R.id.tv_widget_item_name, recipeName);

            //get ingredients of clicked recipe item
            Intent intent = new Intent();
            Bundle extras = new Bundle();
            int recipeId = mCursor.getInt(mCursor.getColumnIndex(DBContract.RecipeEntry.COLUMN_RECIPE_ID));
            extras.putInt(RecipesIngredientsService.EXTRA_RECIPE_ID, recipeId);
            intent.putExtras(extras);
            views.setOnClickFillInIntent(R.id.tv_widget_item_name, intent);
        }

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
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}

