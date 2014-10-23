package hogent.hogentprojecteniii_groep10;

import java.util.List;
import retrofit.client.Response;
import retrofit.http.*;

/**
 * Created by Jaimy Smets on 20/10/2014.
 */
public interface RestService {
    @GET("/users/{user}/repos")
    List<Response> listRepos(@Path("user") String user);

    @GET("/data/2.5/weather")
    WeerOverzicht getWeerOverzicht(@Query("q") String stadEnLand);

    //Een uitgebreid voorbeeld op hoe de formatting werkt. Dit hoort natuurlijk normaal niet in deze klasse te staan.
    class WeerOverzicht {
        Coord coord;
        Sys sys;
        Weather[] weather;
        String base;
        Main main;
        Wind wind;
        Clouds clouds;
        long dt;
        long id;
        String name;
        int cod;
    }

    class Coord {
        double lon;
        double lat;
    }

    class Sys {
        int type;
        int id;
        String message;
        String country;
        int sunrise;
        int sunset;
    }

    class Weather {
        int id;
        String main;
        String description;
        String icon;
    }

    class WeatherData{
        int id;
        String main;
        String description;
        String icon;
    }

    class Main {
        double temp;
        int pressure;
        int humidity;
        double temp_min;
        double temp_max;
    }

    class Wind {
        double speed;
        int deg;
    }

    class Clouds{
        int all;
    }


}
