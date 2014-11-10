package hogent.hogentprojecteniii_groep10.activities;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

import hogent.hogentprojecteniii_groep10.R;
import hogent.hogentprojecteniii_groep10.fragments.VacationDetailFragment;
import hogent.hogentprojecteniii_groep10.models.Vacation;


public class VacationDetailActivity extends FragmentActivity {
    private Vacation vacation;
    private VacationDetailFragment fragmentVacationDetail;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.specific_vacation_view, menu);
        if (getActionBar() != null)
        {
            getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
            getActionBar().setTitle(vacation.getTitle());
        }

        return true;
    }
}
