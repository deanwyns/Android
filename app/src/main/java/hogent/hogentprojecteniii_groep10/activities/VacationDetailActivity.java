package hogent.hogentprojecteniii_groep10.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import hogent.hogentprojecteniii_groep10.R;
import hogent.hogentprojecteniii_groep10.fragments.VacationDetailFragment;
import hogent.hogentprojecteniii_groep10.helpers.RestClient;
import hogent.hogentprojecteniii_groep10.models.Vacation;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * De activity die een specifieke vakantie zal tonen met een fragment.
 */
public class VacationDetailActivity extends FragmentActivity {
    private Vacation vacation;
    private VacationDetailFragment fragmentVacationDetail;

    /**
     * De creatie van deze activity. Zal een vakantie halen uit de parcellable en in de vacationDetailFragment steken.
     * Waarna deze in de activity wordt geplaatst via fragmenttransaction.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_detail);
        // Fetch the item to display from bundle
        vacation = (Vacation) getIntent().getParcelableExtra("SpecificVacation");
        if (savedInstanceState == null) {
            // Insert detail fragment based on the item passed
            fragmentVacationDetail = VacationDetailFragment.newInstance(vacation);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flDetailContainer, fragmentVacationDetail);
            ft.commit();
        }
    }

    /**
     * Bepaald de actie die wordt uitgevoerd bij het uitvoeren van een menu item
     * @param item het item waarop is geklikt
     * @return bepaald hoe verdere menu processing wordt afgehandeld
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settings = new Intent(getApplicationContext(), MainSettingsActivity.class);
            startActivity(settings);
        } else if (id == R.id.thumbs_up) {
            Callback<Response> callback = new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    Toast.makeText(getApplicationContext(), vacation.getTitle() + " heeft een like gekregen!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    Toast.makeText(getApplicationContext(), "U bent niet ingelogd of u heeft reeds een like gegeven!", Toast.LENGTH_SHORT).show();
                }
            };
            SharedPreferences sharedPref = this
                    .getSharedPreferences(this.getString(R.string.authorization_preference_file), Context.MODE_PRIVATE);
            String token = sharedPref.getString(this.getResources().getString(R.string.authorization), "No token");
            RestClient restClient = new RestClient(token);
            restClient.getRestService().likeVacation(vacation.getId(), callback);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Maakt het menu op basis van specific_vacation_view xml.
     * Zal ook het icoon verwijderen en de titel aanpassen.
     *
     * @param menu het menu dat aangepast wordt.
     * @return bepaald hoe verdere menu processing wordt afgehandeld
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.specific_vacation_view, menu);
        if (getActionBar() != null) {
            getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
            getActionBar().setTitle(vacation.getTitle());
        }

        return true;
    }
}
