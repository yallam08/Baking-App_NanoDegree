package nano.yallam.bakingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import nano.yallam.bakingapp.model.Recipe;


public class RecipesListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Recipe> recipes;

    /**
     * Constructor method
     * @param recipesList The list of recipes to display
     */
    public RecipesListAdapter(Context context, ArrayList<Recipe> recipesList) {
        mContext = context;
        recipes = recipesList;
    }

    /**
     * Returns the number of items the adapter will display
     */
    @Override
    public int getCount() {
        return recipes == null ? 0 : recipes.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    /**
     * Creates a new TextView for each item referenced by the adapter
     */
    public View getView(final int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        TextView tv;
        if (convertView == null) {
            tv = (TextView) inflater.inflate(R.layout.recipes_list_item, parent, false);
        } else {
            tv = (TextView) convertView;
        }

        tv.setText(recipes.get(position).getName());
        return tv;
    }

    public void setRecipes(ArrayList<Recipe> recipesList) {
        recipes = recipesList;
        notifyDataSetChanged();
    }

    public ArrayList<Recipe> getRecipes() {
        return recipes;
    }
}
