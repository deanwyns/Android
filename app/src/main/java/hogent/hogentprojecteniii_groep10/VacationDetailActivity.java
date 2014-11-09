package hogent.hogentprojecteniii_groep10;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import hogent.hogentprojecteniii_groep10.models.Vacation;


public class VacationDetailActivity extends FragmentActivity {

    private VacationDetailFragment fragmentVacationDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_detail);
        // Fetch the item to display from bundle
        Vacation vacation = (Vacation) getIntent().getParcelableExtra("SpecificVacation");
        if (savedInstanceState == null) {
            // Insert detail fragment based on the item passed
            fragmentVacationDetail = VacationDetailFragment.newInstance(vacation);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flDetailContainer, fragmentVacationDetail);
            ft.commit();
        }
    }
}
