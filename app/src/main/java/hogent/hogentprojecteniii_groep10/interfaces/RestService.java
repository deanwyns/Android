package hogent.hogentprojecteniii_groep10.interfaces;

import java.util.List;
import java.util.Map;

import hogent.hogentprojecteniii_groep10.models.Gebruiker;
import hogent.hogentprojecteniii_groep10.models.LoginToken;
import hogent.hogentprojecteniii_groep10.models.VacationResponse;
import hogent.hogentprojecteniii_groep10.models.WeerOverzichtVoorbeeld;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.*;


public interface RestService {
    @GET("/users/{user}/repos")
    List<Response> listRepos(@Path("user") String user);

    @GET("/data/2.5/weather")
    WeerOverzichtVoorbeeld getWeerOverzicht(@Query("q") String stadEnLand);

    @GET("/vacation")
    VacationResponse getVacationOverview();

    @FormUrlEncoded
    @POST("/access_token")
    LoginToken login(@FieldMap Map<String, String> options);

    @FormUrlEncoded
    @POST("/user")
    void register(@FieldMap Map<String, String> options, Callback<String> callback);

}
