package nano.yallam.bakingapp.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import nano.yallam.bakingapp.provider.DBContract.RecipeEntry;
import nano.yallam.bakingapp.provider.DBContract.StepEntry;
import nano.yallam.bakingapp.provider.DBContract.IngredientEntry;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bakingapp.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RECIPE_TABLE = "CREATE TABLE " + RecipeEntry.TABLE_NAME + " ( " +
                RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RecipeEntry.COLUMN_RECIPE_ID + " INTEGER NOT NULL, " +
                RecipeEntry.COLUMN_NAME + " VARCHAR(255) NOT NULL, " +
                RecipeEntry.COLUMN_SERVINGS + " INTEGER, " +
                RecipeEntry.COLUMN_IMAGE + " TEXT)";

        String CREATE_STEP_TABLE = "CREATE TABLE " + StepEntry.TABLE_NAME + " ( " +
                StepEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                StepEntry.COLUMN_STEP_ID + " INTEGER NOT NULL, " +
                StepEntry.COLUMN_RECIPE_ID + " INTEGER NOT NULL, " +
                StepEntry.COLUMN_SHORT_DESCRIPTION + " VARCHAR(255), " +
                StepEntry.COLUMN_DESCRIPTION + " TEXT, " +
                StepEntry.COLUMN_VIDEO_URL + " VARCHAR(255), " +
                StepEntry.COLUMN_THUMB_URL + " VARCHAR(255)) ";

        String CREATE_INGREDIENT_TABLE = "CREATE TABLE " + IngredientEntry.TABLE_NAME + " ( " +
                IngredientEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                IngredientEntry.COLUMN_RECIPE_ID + " INTEGER NOT NULL, " +
                IngredientEntry.COLUMN_MEASURE + " VARCHAR(255), " +
                IngredientEntry.COLUMN_INGREDIENT + " VARCHAR(255), " +
                IngredientEntry.COLUMN_QUANTITY + " REAL)";

        db.execSQL(CREATE_RECIPE_TABLE);
        db.execSQL(CREATE_STEP_TABLE);
        db.execSQL(CREATE_INGREDIENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.onCreate(db);
    }
}
