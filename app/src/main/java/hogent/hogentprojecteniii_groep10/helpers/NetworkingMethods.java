package hogent.hogentprojecteniii_groep10.helpers;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Hulpklasse ivm netwerken
 */
public class NetworkingMethods {
    /**
     * Bekijkt of er netwerk is op het huidige toestel
     * @param context de applicatiecontext
     * @return true als er internet aanwezig is
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
