package nl.evertwoud.issapp.data.network;

import nl.evertwoud.issapp.data.models.ISSPosition;
import nl.evertwoud.issapp.data.models.Location;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface APIService {

    String BASE_URL = "http://api.open-notify.org/";

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("iss-now")
    Call<Location> getLocation();

}