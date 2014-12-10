package hogent.hogentprojecteniii_groep10.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import hogent.hogentprojecteniii_groep10.R;
import retrofit.RequestInterceptor;

/**
 * Hulpklasse om een header toe te voegen aan de URL van het http request
 */
public class SessionRequestInterceptor implements RequestInterceptor {

    private String token;

    public SessionRequestInterceptor(String token) {
        this.token = token;
    }

    /**
     * Voegt een authorization header toe aan het request als de gebruiker ingelogd is
     * @param request
     */
    @Override
    public void intercept(RequestFacade request) {
        if(!this.token.equals("") ||this.token!= null)
        request.addHeader("Authorization", token);
    }
}
