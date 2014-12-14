package hogent.hogentprojecteniii_groep10.helpers;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import hogent.hogentprojecteniii_groep10.R;

/**
 * Klasse met extra hulp methodes
 */
public class HelperMethods {
    /**
     * Bekijkt of er netwerk is op het huidige toestel
     * @param context de applicatiecontext
     * @return true als er internet aanwezig is
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null) && activeNetwork.isConnectedOrConnecting();
    }

    /**
     * Kijkt of er een token aanwezig is, dus of er ingelogd is of niet.
     * @param context de context van de applicatie
     * @return true als er een token bestaat, dus ingelogd is.
     */
    public static boolean isLoggedIn(Context context){
        boolean isLoggedIn;
        SharedPreferences sharedPref = context
                .getSharedPreferences(context.getString(R.string.authorization_preference_file), Context.MODE_PRIVATE);
        String token = sharedPref.getString(context.getResources().getString(R.string.authorization), "No token");
        isLoggedIn = !token.equals("No token");

        //TODO: nakijken of token nog geldig is

        return isLoggedIn;
    }
}
