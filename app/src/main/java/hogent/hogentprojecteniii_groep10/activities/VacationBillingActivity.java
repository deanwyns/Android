package hogent.hogentprojecteniii_groep10.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hogent.hogentprojecteniii_groep10.R;
import hogent.hogentprojecteniii_groep10.models.Gebruiker;
import hogent.hogentprojecteniii_groep10.models.Kind;
import hogent.hogentprojecteniii_groep10.models.Vacation;

/**
 * Laat toe om betalingsgegevens in te vullen tijdens het inschrijven.
 * Implementeert textwatcher om controle te doen op de gegevens.
 */
public class VacationBillingActivity extends Activity implements TextWatcher {

    private Vacation selectedVacation;
    private Kind[] signedUpChildren;
    private EditText streetTxt, postalCodeTxt, houseNumberTxt, cityTxt, firstnameTxt, nameTxt;
    private Button goToBillingOverviewBtn;

    /**
     * Initialiseert de activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_billing);

        selectedVacation = getIntent().getParcelableExtra("SpecificVacation");
        Parcelable[] parcelableArray = getIntent().getParcelableArrayExtra("SignedUpChildren");
        if (parcelableArray != null) {
            signedUpChildren = Arrays.copyOf(parcelableArray, parcelableArray.length, Kind[].class);
        }

        firstnameTxt = (EditText) findViewById(R.id.billing_firstname_txt);
        firstnameTxt.addTextChangedListener(this);
        nameTxt = (EditText) findViewById(R.id.billing_lastname_txt);
        nameTxt.addTextChangedListener(this);
        streetTxt = (EditText) findViewById(R.id.street_txt);
        streetTxt.addTextChangedListener(this);
        postalCodeTxt = (EditText) findViewById(R.id.postal_code_txt);
        postalCodeTxt.addTextChangedListener(this);
        houseNumberTxt = (EditText) findViewById(R.id.housenumber_txt);
        houseNumberTxt.addTextChangedListener(this);
        cityTxt = (EditText) findViewById(R.id.city_txt);
        cityTxt.addTextChangedListener(this);

        goToBillingOverviewBtn = (Button) findViewById(R.id.go_to_billing_overview_btn);

        goToBillingOverviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateTextFields()) {
                    Intent billingOverview = new Intent(getApplicationContext(), VacationSignupOverviewActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putParcelable("SpecificVacation", selectedVacation);
                    mBundle.putParcelableArray("SignedUpChildren", signedUpChildren);
                    mBundle.putString("firstnameTxt", firstnameTxt.getText().toString());
                    mBundle.putString("nameTxt", nameTxt.getText().toString());
                    mBundle.putString("housenumberTxt", houseNumberTxt.getText().toString());
                    mBundle.putString("postalCodeTxt", postalCodeTxt.getText().toString());
                    mBundle.putString("cityTxt", cityTxt.getText().toString());
                    mBundle.putString("streetTxt", streetTxt.getText().toString());
                    billingOverview.putExtras(mBundle);
                    startActivity(billingOverview);
                } else {
                    Toast wrongInput = Toast.makeText(getApplicationContext(), R.string.wrong_input_on_billing, Toast.LENGTH_LONG);
                    wrongInput.show();
                }
            }
        });

        setupSecretButton();
    }

    /**
     * Verborgen 'knop' om snel gegevens te kunnen invoeren tijdens de presentatie
     */
    private void setupSecretButton() {
        TextView betGegevens = (TextView) findViewById(R.id.billing_title);
        betGegevens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstnameTxt.setText("Geert");
                nameTxt.setText("De Mei");
                streetTxt.setText("Stationstraat");
                houseNumberTxt.setText("12");
                postalCodeTxt.setText("900");
                cityTxt.setText("Gent");
            }
        });
    }

    /**
     * Zal het menu aanmaken van deze activity
     * @param menu het menu dat wordt aangepast
     * @return bepaald hoe verdere menu processing wordt afgehandeld
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.vacation_billing, menu);
        if( getActionBar() != null)
            getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        return true;
    }

    /**
     * Zal de textfields valideren en fouten weergeven.
     * @return true als alle velden correct zijn ingevuld
     */
    private boolean validateTextFields() {
        Pattern postalCodePattern = Pattern.compile("^[1-9][0-9]{3}$");
        Matcher postalCodeMatcher = postalCodePattern.matcher(postalCodeTxt.getText().toString());

        Pattern HousenumberPattern = Pattern.compile("^\\d+([a-zA-Z])?$");
        Matcher HousenumberMatcher = HousenumberPattern.matcher(houseNumberTxt.getText().toString());

        setTextColor(postalCodeTxt, postalCodeMatcher.matches());
        setTextColor(houseNumberTxt, HousenumberMatcher.matches());
        setTextColor(cityTxt, !cityTxt.toString().isEmpty());
        setTextColor(streetTxt, !streetTxt.toString().isEmpty());
        setTextColor(firstnameTxt, !firstnameTxt.getText().toString().isEmpty());
        setTextColor(nameTxt, !nameTxt.getText().toString().isEmpty());

        return HousenumberMatcher.matches() && HousenumberMatcher.matches() &&
                !cityTxt.toString().isEmpty() && !streetTxt.toString().isEmpty() &&
                !firstnameTxt.getText().toString().isEmpty() && !nameTxt.getText().toString().isEmpty();
    }

    /**
     * Hulpmethode om textkleur van invoervakken aan te passen op basis van hun status
     * @param textView de textview die aangepast wordt
     * @param matched de status of de text correct is
     */
    private void setTextColor(TextView textView, boolean matched) {
        if(!matched)
            textView.setTextColor(Color.RED);
        else
            textView.setTextColor(Color.rgb(80, 200, 120));
    }


    /**
     * Standaard implementatie van de interface. Wordt niet gebruikt.
     * @param s
     * @param start
     * @param count
     * @param after
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    /**
     * Standaard implementatie van de interface. Wordt niet gebruikt.
     * @param s
     * @param start
     * @param before
     * @param count
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    /**
     * Zal na elke text verandering de textfields controleren.
     * @param s de waarde die verandert werd.
     */
    @Override
    public void afterTextChanged(Editable s) {
        validateTextFields();
    }
}
