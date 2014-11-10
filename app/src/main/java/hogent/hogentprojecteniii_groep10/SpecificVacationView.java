package hogent.hogentprojecteniii_groep10;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import hogent.hogentprojecteniii_groep10.models.Vacation;


public class SpecificVacationView extends Activity {

    private Vacation selectedVacation;
    private TextView vacationTitleTextView, vacationPromotionalTextTextView, vacationDescriptionTextView,
            vacationDestinationTextView, vacationAgeTextView, vacationWhenTextView,
            vacationTransportationTextView, vacationParticipantsTextView, vacationPriceTextView,
            vacationDiscountPriceTextView, vacationTaxDeductableTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_vacation_view);

        selectedVacation = (Vacation) getIntent().getParcelableExtra("SpecificVacation");

        vacationTitleTextView = (TextView) findViewById(R.id.specific_vacation_title);
        vacationPromotionalTextTextView = (TextView) findViewById(R.id.specific_vacation_promotext_text);
        vacationDescriptionTextView = (TextView) findViewById(R.id.specific_vacation_description_text);
        vacationDestinationTextView = (TextView) findViewById(R.id.specific_vacation_destination_text);
        vacationAgeTextView = (TextView) findViewById(R.id.specific_vacation_age_text);
        vacationWhenTextView = (TextView) findViewById(R.id.specific_vacation_when_text);
        vacationTransportationTextView = (TextView) findViewById(R.id.specific_vacation_transportation_text);
        vacationParticipantsTextView = (TextView) findViewById(R.id.specific_vacation_participants_text);
        vacationPriceTextView = (TextView) findViewById(R.id.specific_vacation_price_text);
        vacationDiscountPriceTextView = (TextView) findViewById(R.id.specific_vacation_discount_price_text);
        vacationTaxDeductableTextView = (TextView) findViewById(R.id.specific_vacation_tax_deductable_text);

        vacationTitleTextView.setText(selectedVacation.getTitle());
        vacationPromotionalTextTextView.setText(selectedVacation.getPromoText());
        vacationDescriptionTextView.setText(selectedVacation.getDescription());
        vacationDestinationTextView.setText(selectedVacation.getLocation());
        vacationAgeTextView.setText(String.format("%d - %d", selectedVacation.getAgeFrom(), selectedVacation.getAgeTo()));
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        vacationWhenTextView.setText(String.format("%s - %s", formatter.format(selectedVacation.getBeginDate().getTime()), formatter.format(selectedVacation.getEndDate().getTime())));
        vacationTransportationTextView.setText(selectedVacation.getTransportation());
        vacationParticipantsTextView.setText(String.format("?/%d", selectedVacation.getMaxParticipants()));
        vacationPriceTextView.setText(String.format("€%.2f", selectedVacation.getBaseCost()));
        vacationDiscountPriceTextView.setText(String.format("%s €%.2f\n%s €%.2f", R.string.eenOuderLidBM,
                selectedVacation.getOneBmMemberCost(), R.string.beideOudersLidBM, selectedVacation.getTwoBmMemberCost()));
        vacationTaxDeductableTextView.setText((selectedVacation.isTaxDeductable()==1 ? R.string.ja : R.string.nee));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.specific_vacation_view, menu);
        if (getActionBar() != null) {
            getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
            getActionBar().setTitle(selectedVacation.getTitle());
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.thumbs_up) {
            return true;
        } else if (id == R.id.thumbs_down) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
