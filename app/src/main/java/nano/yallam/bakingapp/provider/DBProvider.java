package nano.yallam.bakingapp.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

import nano.yallam.bakingapp.provider.DBContract.IngredientEntry;
import nano.yallam.bakingapp.provider.DBContract.RecipeEntry;
import nano.yallam.bakingapp.provider.DBContract.StepEntry;


public class DBProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DBHelper mOpenHelper;

    static final int ALL_RECIPES = 100;
    static final int ONE_RECIPE = 101;
    static final int ALL_STEPS = 102;
    static final int ONE_STEP = 103;
    static final int ALL_INGREDIENTS = 104;
    static final int ONE_INGREDIENT = 105;


    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = DBContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, DBContract.PATH_RECIPES, ALL_RECIPES);
        matcher.addURI(authority, DBContract.PATH_RECIPES + "/#", ONE_RECIPE);

        matcher.addURI(authority, DBContract.PATH_STEPS, ALL_STEPS);
        matcher.addURI(authority, DBContract.PATH_STEPS + "/#", ONE_STEP);

        matcher.addURI(authority, DBContract.PATH_INGREDIENTS, ALL_INGREDIENTS);
        matcher.addURI(authority, DBContract.PATH_INGREDIENTS + "/#", ONE_INGREDIENT);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DBHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case ALL_RECIPES:
                return RecipeEntry.CONTENT_TYPE;
            case ONE_RECIPE:
                return RecipeEntry.CONTENT_ITEM_TYPE;
            case ALL_STEPS:
                return StepEntry.CONTENT_TYPE;
            case ONE_STEP:
                return StepEntry.CONTENT_ITEM_TYPE;
            case ALL_INGREDIENTS:
                return IngredientEntry.CONTENT_TYPE;
            case ONE_INGREDIENT:
                return IngredientEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "recipe"
            case ALL_RECIPES: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        RecipeEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "recipe/#"
            case ONE_RECIPE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        RecipeEntry.TABLE_NAME,
                        projection,
                        RecipeEntry.COLUMN_RECIPE_ID + " = ?",
                        new String[]{uri.getPathSegments().get(1)},
                        null,
                        null,
                        null
                );
                break;
            }
            // "ingredient"
            case ALL_INGREDIENTS: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        IngredientEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "ingredient/#"
            case ONE_INGREDIENT: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        IngredientEntry.TABLE_NAME,
                        projection,
                        IngredientEntry._ID + " = ?",
                        new String[]{uri.getPathSegments().get(1)},
                        null,
                        null,
                        null
                );
                break;
            }
            // "step"
            case ALL_STEPS: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        StepEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            // "step/#"
            case ONE_STEP: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        StepEntry.TABLE_NAME,
                        projection,
                        StepEntry.COLUMN_STEP_ID + " = ?",
                        new String[]{uri.getPathSegments().get(1)},
                        null,
                        null,
                        null
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri = null;

        switch (match) {
            case ALL_RECIPES: {
                long id = db.insert(RecipeEntry.TABLE_NAME, null, values);
                if (id > 0)
                    returnUri = RecipeEntry.buildRecipeUri((int) values.get(RecipeEntry.COLUMN_RECIPE_ID));
                else
                    throw new UnsupportedOperationException("Failed to insert into uri: " + uri);
                break;
            }
            case ALL_STEPS: {
                long id = db.insert(StepEntry.TABLE_NAME, null, values);
                if (id > 0)
                    returnUri = RecipeEntry.buildRecipeUri((int) values.get(StepEntry.COLUMN_STEP_ID));
                else
                    throw new UnsupportedOperationException("Failed to insert into uri: " + uri);
                break;
            }
            case ALL_INGREDIENTS: {
                long id = db.insert(IngredientEntry.TABLE_NAME, null, values);
                if (id > 0)
                    returnUri = RecipeEntry.buildRecipeUri((int) id);
                else
                    throw new UnsupportedOperationException("Failed to insert into uri: " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case ALL_RECIPES:
                db.beginTransaction();
                int rowsInserted = 0;
                try {
                    for (ContentValues value : values) {
                        try {
                            long id = db.insertOrThrow(RecipeEntry.TABLE_NAME, null, value);
                            rowsInserted++;
                        } catch (SQLException e) {
                            rowsInserted++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                getContext().getContentResolver().notifyChange(uri, null);

                return rowsInserted;

            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        if (null == selection) selection = "1";
        switch (match) {
            case ALL_RECIPES:
                rowsDeleted = db.delete(
                        RecipeEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ONE_RECIPE:
                rowsDeleted = db.delete(
                        RecipeEntry.TABLE_NAME, RecipeEntry.COLUMN_RECIPE_ID + " = ?",
                        new String[]{uri.getPathSegments().get(1)}
                );
                break;
            case ALL_STEPS:
                rowsDeleted = db.delete(
                        StepEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ONE_STEP:
                rowsDeleted = db.delete(
                        StepEntry.TABLE_NAME, StepEntry.COLUMN_STEP_ID + " = ?",
                        new String[]{uri.getPathSegments().get(1)}
                );
                break;
            case ALL_INGREDIENTS:
                rowsDeleted = db.delete(
                        IngredientEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ONE_INGREDIENT:
                rowsDeleted = db.delete(
                        IngredientEntry.TABLE_NAME, IngredientEntry.COLUMN_RECIPE_ID + " = ?",
                        new String[]{uri.getPathSegments().get(1)}
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case ALL_RECIPES:
                rowsUpdated = db.update(RecipeEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case ALL_STEPS:
                rowsUpdated = db.update(StepEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case ALL_INGREDIENTS:
                rowsUpdated = db.update(IngredientEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}