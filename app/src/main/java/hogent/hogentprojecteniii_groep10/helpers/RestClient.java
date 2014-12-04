package hogent.hogentprojecteniii_groep10.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import hogent.hogentprojecteniii_groep10.R;
import hogent.hogentprojecteniii_groep10.interfaces.RestService;
import retrofit.RestAdapter;

/**
 * Created by Fabrice on 2/12/2014.
 */
public class RestClient {

    private static final String BASE_URL = "http://lloyd.deanwyns.me/api";
    private RestService restService;

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

    public RestService getRestService(){
        return restService;
    }


}
