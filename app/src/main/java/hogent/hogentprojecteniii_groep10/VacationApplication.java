package hogent.hogentprojecteniii_groep10;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class VacationApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        clearAccessToken();
    }

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
