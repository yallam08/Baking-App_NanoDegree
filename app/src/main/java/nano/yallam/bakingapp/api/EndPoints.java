package nano.yallam.bakingapp.api;

import java.util.ArrayList;

import nano.yallam.bakingapp.model.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;

public interface EndPoints {

    @GET("android-baking-app-json")
    Call<ArrayList<Recipe>> getRecipes();

}
