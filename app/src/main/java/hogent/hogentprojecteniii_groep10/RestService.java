package hogent.hogentprojecteniii_groep10;

import java.util.List;

import hogent.hogentprojecteniii_groep10.models.WeerOverzichtVoorbeeld;
import retrofit.client.Response;
import retrofit.http.*;

/**
 * Created by Jaimy Smets on 20/10/2014.
 */
public interface RestService {
    @GET("/users/{user}/repos")
    List<Response> listRepos(@Path("user") String user);

    @GET("/data/2.5/weather")
    WeerOverzichtVoorbeeld getWeerOverzicht(@Query("q") String stadEnLand);



}
