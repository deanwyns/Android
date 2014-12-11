package hogent.hogentprojecteniii_groep10.activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import hogent.hogentprojecteniii_groep10.R;
import hogent.hogentprojecteniii_groep10.fragments.VacationDetailFragment;
import hogent.hogentprojecteniii_groep10.models.Vacation;

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
            //TODO: geef de vakantie een thumbs up
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
