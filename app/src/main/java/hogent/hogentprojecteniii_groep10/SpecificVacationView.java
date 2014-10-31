package hogent.hogentprojecteniii_groep10;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import hogent.hogentprojecteniii_groep10.models.Vacation;


public class SpecificVacationView extends Activity {

    private Vacation selectedVacation;
    private TextView vacationTitleTextView, vacationDescriptionTextView, vacationPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_vacation_view);

        selectedVacation = (Vacation) getIntent().getParcelableExtra("SpecificVacation");
        vacationTitleTextView = (TextView) findViewById(R.id.specific_vacation_title);
        vacationDescriptionTextView = (TextView) findViewById(R.id.specific_vacation_description_text);
        vacationPriceTextView = (TextView) findViewById(R.id.specific_vacation_price_text);

        vacationTitleTextView.setText(selectedVacation.getTitle());
        vacationDescriptionTextView.setText(selectedVacation.getDescription());
        vacationPriceTextView.setText(String.format("€%.2f", selectedVacation.getBaseCost()));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.specific_vacation_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
