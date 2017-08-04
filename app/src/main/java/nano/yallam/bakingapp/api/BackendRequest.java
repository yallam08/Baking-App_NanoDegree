package nano.yallam.bakingapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BackendRequest {
    public static EndPoints createRequest() {

        Retrofit.Builder rBuilder = new Retrofit.Builder()
                .baseUrl("http://go.udacity.com/");

        Retrofit retrofit = rBuilder.addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(EndPoints.class);
    }
}
