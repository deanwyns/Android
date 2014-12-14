package hogent.hogentprojecteniii_groep10.main;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import hogent.hogentprojecteniii_groep10.R;

/**
 * Om de inlogtoken te verwijderen bij elke applicatiestart, is er een custom applicatie starter klasse.
 */
public class VacationApplication extends Application {
    /**
     * De methode die de activity creëert.
     */
    @Override
    public void onCreate() {
        super.onCreate();

        //clearAccessToken();
    }

    /**
     * Bij het opstarten van de applicatie zal de token verwijderd worden.
     * Hierdoor zullen er geen oude tokens in de applicatie zitten.
     */
    private void clearAccessToken() {
        SharedPreferences sharedPref = this
                .getSharedPreferences(getString(R.string.authorization_preference_file), Context.MODE_PRIVATE);
        if(sharedPref.contains(getResources().getString(R.string.authorization))){
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.remove(getResources().getString(R.string.authorization));
            editor.apply();
        }
    }
}
