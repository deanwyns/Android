package hogent.hogentprojecteniii_groep10.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import hogent.hogentprojecteniii_groep10.R;
import hogent.hogentprojecteniii_groep10.interfaces.RestService;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * De hulpklasse om de restService aan te maken
 */
public class RestClient {

    private static final String BASE_URL = "http://lloyd.deanwyns.me/api";
    private RestService restService;

    /**
     * Als de gebruiker ingelogd moet zijn zal hier het token naar de interceptor gestuurd worden
     * @param token de string die het token zou moeten bevatten
     */
    public RestClient(String token){

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setRequestInterceptor(new SessionRequestInterceptor(token))
                .setEndpoint(BASE_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        restService = restAdapter.create(RestService.class);
    }

    public RestClient(){

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        restService = restAdapter.create(RestService.class);
    }

    public RestClient(String token, Gson gson){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setRequestInterceptor(new SessionRequestInterceptor(token))
                .setConverter(new GsonConverter(gson))
                .build();
        restService = restAdapter.create(RestService.class);
    }

    public RestService getRestService(){
        return restService;
    }


}
