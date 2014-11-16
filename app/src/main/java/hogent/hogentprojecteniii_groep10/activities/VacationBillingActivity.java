package hogent.hogentprojecteniii_groep10.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Toast;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hogent.hogentprojecteniii_groep10.R;
import hogent.hogentprojecteniii_groep10.models.Gebruiker;
import hogent.hogentprojecteniii_groep10.models.Vacation;

public class VacationBillingActivity extends Activity implements TextWatcher {

    private Vacation selectedVacation;
    private Gebruiker[] signedUpChildren;
    private EditText streetAndHousenumberTxt, postalCodeAndCityTxt, firstnameTxt, nameTxt;
    private Button goToBillingOverviewBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_billing);

        selectedVacation = getIntent().getParcelableExtra("SpecificVacation");
        Parcelable[] parcelableArray = getIntent().getParcelableArrayExtra("SignedUpChildren");
        if (parcelableArray != null) {
            signedUpChildren = Arrays.copyOf(parcelableArray, parcelableArray.length, Gebruiker[].class);
        }

        firstnameTxt = (EditText) findViewById(R.id.billing_firstname_txt);
        firstnameTxt.addTextChangedListener(this);
        nameTxt = (EditText) findViewById(R.id.billing_lastname_txt);
        nameTxt.addTextChangedListener(this);
        streetAndHousenumberTxt = (EditText) findViewById(R.id.street_and_housenumber_txt);
        streetAndHousenumberTxt.addTextChangedListener(this);
        postalCodeAndCityTxt = (EditText) findViewById(R.id.postal_code_and_city_txt);
        postalCodeAndCityTxt.addTextChangedListener(this);
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
                    mBundle.putString("streetAndHousenumberTxt", streetAndHousenumberTxt.getText().toString());
                    mBundle.putString("postalCodeAndCityTxt", postalCodeAndCityTxt.getText().toString());
                    billingOverview.putExtras(mBundle);
                    startActivity(billingOverview);
                } else {
                    Toast wrongInput = Toast.makeText(getApplicationContext(), R.string.wrong_input_on_billing, Toast.LENGTH_LONG);
                    wrongInput.show();
                }
            }
        });

    }

    private boolean validateTextFields() {
        Pattern postalCodeAndCityPattern = Pattern.compile("^[1-9][0-9]{3} [a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$");
        Matcher postalCodeAndCityMatcher = postalCodeAndCityPattern.matcher(postalCodeAndCityTxt.getText().toString());

        Pattern streetAndHousenumberPattern = Pattern.compile("^((.){1,}(\\d){1,}(.){0,})$");
        Matcher streetAndHousenumberMatcher = streetAndHousenumberPattern.matcher(streetAndHousenumberTxt.getText().toString());

        if (!postalCodeAndCityMatcher.matches())
            postalCodeAndCityTxt.setTextColor(Color.RED);
        else
            postalCodeAndCityTxt.setTextColor(Color.rgb(80, 200, 120));

        if (!streetAndHousenumberMatcher.matches())
            streetAndHousenumberTxt.setTextColor(Color.RED);
        else
            streetAndHousenumberTxt.setTextColor(Color.rgb(80, 200, 120));

        if (firstnameTxt.getText().toString().isEmpty())
            firstnameTxt.setTextColor(Color.RED);
        else
            firstnameTxt.setTextColor(Color.rgb(80, 200, 120));

        if (nameTxt.getText().toString().isEmpty())
            nameTxt.setTextColor(Color.RED);
        else
            nameTxt.setTextColor(Color.rgb(80, 200, 120));

        return postalCodeAndCityMatcher.matches() && streetAndHousenumberMatcher.matches() && !firstnameTxt.getText().toString().isEmpty() && !nameTxt.getText().toString().isEmpty();
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        validateTextFields();
    }
}
