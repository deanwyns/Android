package hogent.hogentprojecteniii_groep10.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import hogent.hogentprojecteniii_groep10.R;
import hogent.hogentprojecteniii_groep10.main.Main;
import hogent.hogentprojecteniii_groep10.models.Gebruiker;
import hogent.hogentprojecteniii_groep10.models.Vacation;

public class VacationSignupOverviewActivity extends Activity {

    private Vacation selectedVacation;
    private Gebruiker[] signedUpChildren;
    private String streetAndHousenumberTxt, postalCodeAndCityTxt, firstnameTxt, nameTxt;
    private TextView signupOverviewVacationTitleLbl, signupOverviewVacationDateLbl,
            signupOverviewVacationPriceLbl, signupOverviewVacationTotalPriceLbl,
            signupOverviewBillingNameLbl, signupOverviewBillingAddressStreetHousenumberLbl,
            signupOverviewBillingAddressPostalcodeCityLbl;
    private LinearLayout signupOverviewSignupsLinearLayout;
    private Button cancelBtn, signupBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_signup_overview);

        selectedVacation = getIntent().getParcelableExtra("SpecificVacation");
        Parcelable[] parcelableArray = getIntent().getParcelableArrayExtra("SignedUpChildren");
        if (parcelableArray != null) {
            signedUpChildren = Arrays.copyOf(parcelableArray, parcelableArray.length, Gebruiker[].class);
        }
        streetAndHousenumberTxt = getIntent().getStringExtra("streetAndHousenumberTxt");
        postalCodeAndCityTxt = getIntent().getStringExtra("postalCodeAndCityTxt");
        firstnameTxt = getIntent().getStringExtra("firstnameTxt");
        nameTxt = getIntent().getStringExtra("nameTxt");

        signupOverviewVacationTitleLbl = (TextView) findViewById(R.id.signup_overview_vacation_title_lbl);
        signupOverviewVacationDateLbl = (TextView) findViewById(R.id.signup_overview_vacation_date_lbl);
        signupOverviewVacationPriceLbl = (TextView) findViewById(R.id.signup_overview_vacation_price_lbl);
        signupOverviewVacationTotalPriceLbl = (TextView) findViewById(R.id.signup_overview_vacation_total_price_lbl);
        signupOverviewBillingNameLbl = (TextView) findViewById(R.id.signup_overview_billing_name_lbl);
        signupOverviewBillingAddressStreetHousenumberLbl = (TextView) findViewById(R.id.signup_overview_billing_address_street_housenumber_lbl);
        signupOverviewBillingAddressPostalcodeCityLbl = (TextView) findViewById(R.id.signup_overview_billing_address_postalcode_city_lbl);
        signupOverviewSignupsLinearLayout = (LinearLayout) findViewById(R.id.signup_overview_signups_linear_layout);
        cancelBtn = (Button) findViewById(R.id.signup_overview_cancel_btn);
        signupBtn = (Button) findViewById(R.id.signup_overview_signup_btn);

        signupOverviewVacationTitleLbl.append(" " + selectedVacation.getTitle());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        signupOverviewVacationDateLbl.append(String.format(" %s - %s", formatter.format(selectedVacation.getBeginDate().getTime()), formatter.format(selectedVacation.getEndDate().getTime())));
        signupOverviewVacationPriceLbl.append(String.format(" €%.2f", selectedVacation.getBaseCost()));
        signupOverviewVacationTotalPriceLbl.append(String.format(" €%.2f", selectedVacation.getBaseCost() * signedUpChildren.length));
        signupOverviewBillingNameLbl.setText(firstnameTxt + " " + nameTxt);
        signupOverviewBillingAddressStreetHousenumberLbl.setText(streetAndHousenumberTxt);
        signupOverviewBillingAddressPostalcodeCityLbl.setText(postalCodeAndCityTxt);

        for(Gebruiker child : signedUpChildren){
            TextView childTextView = new TextView(getApplicationContext());
            childTextView.setText(child.getVoornaam() + " " + child.getNaam());
            childTextView.setTextColor(Color.BLACK);
            signupOverviewSignupsLinearLayout.addView(childTextView);
        }

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Main.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:Server stuff

                Intent intent = new Intent(getApplicationContext(), Main.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
