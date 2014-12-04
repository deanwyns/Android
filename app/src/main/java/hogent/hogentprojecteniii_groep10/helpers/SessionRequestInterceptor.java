package hogent.hogentprojecteniii_groep10.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import hogent.hogentprojecteniii_groep10.R;
import retrofit.RequestInterceptor;

/**
 * Created by Fabrice on 2/12/2014.
 */
public class SessionRequestInterceptor implements RequestInterceptor {

    private String token;

    public SessionRequestInterceptor(String token) {
        this.token = token;
    }

    @Override
    public void intercept(RequestFacade request) {
        if(!this.token.equals("") ||this.token!= null)
        request.addHeader("Authorization", token);
    }
}
