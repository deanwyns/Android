package hogent.hogentprojecteniii_groep10.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import hogent.hogentprojecteniii_groep10.R;
import hogent.hogentprojecteniii_groep10.activities.FindMonitorActivity;
import hogent.hogentprojecteniii_groep10.activities.MainSettingsActivity;
import hogent.hogentprojecteniii_groep10.activities.VacationsListActivity;
import hogent.hogentprojecteniii_groep10.authentication.HomeScreenActivity;
import hogent.hogentprojecteniii_groep10.authentication.Login;
import hogent.hogentprojecteniii_groep10.helpers.HelperMethods;

/**
 * De eerste activity die zal gestart worden. Dit toont het hoofdvenster.
 */
public class Main extends Activity {

    private LinearLayout ll;
    private ImageButton getVacationsBtn, getPhotosBtn, getRegistrationsBtn;
    private final static String TAG = "MAIN";
    private ProgressDialog progress;

    /**
     * Maakt het initiële scherm aan van de applicatie
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
//        setSupportActionBar(toolbar);
        ll = (LinearLayout) findViewById(R.id.newsfeed_id);
        getVacationsBtn = (ImageButton) findViewById(R.id.camp_btn);
        getPhotosBtn = (ImageButton) findViewById(R.id.photo_btn);
        getRegistrationsBtn = (ImageButton) findViewById(R.id.registrations_btn);

        setupListeners();

    }

    /**
     * Afhankelijk van het ingelogd zijn worden er knoppen verborgen
     * @param isLoggedIn bepaald of knoppen verborgen of getoond moeten worden.
     */
    private void showButtonsForLoggedIn(boolean isLoggedIn) {
        if(!isLoggedIn){
            getPhotosBtn.setVisibility(View.INVISIBLE);
            getRegistrationsBtn.setVisibility(View.INVISIBLE);
        }else{
            getPhotosBtn.setVisibility(View.VISIBLE);
            getRegistrationsBtn.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Indien de applicatie verder gaat, moet er gekeken worden of de token nog niet verlopen is.
     */
    @Override
    protected void onResume() {
        super.onResume();
        showButtonsForLoggedIn(HelperMethods.isLoggedIn(getApplication()));
    }

    /**
     * Bereid de listeners voor, voor de knoppen.
     */
    private void setupListeners() {
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Newsfeed clicked");
            }
        });

        getVacationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent vacationOverviewIntent = new Intent(getApplicationContext(), VacationsListActivity.class);
                startActivity(vacationOverviewIntent);
            }
        });

        getPhotosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    /**
     * Geeft de activity een menu
     * @param menu het menu dat de activity zal opvullen
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Bepaalt de actie dat er gebeurt bij het klikken op een item in het menu.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settings = new Intent(getApplicationContext(), MainSettingsActivity.class);
            startActivity(settings);
        } else if (id == R.id.action_login) {
            Intent login = new Intent(getApplicationContext(), HomeScreenActivity.class);
            startActivity(login);
        } else if (id == R.id.action_find_monitor){
            Intent findMonitor = new Intent(getApplicationContext(), FindMonitorActivity.class);
            startActivity(findMonitor);
        }
        return super.onOptionsItemSelected(item);
    }
}
