package hogent.hogentprojecteniii_groep10.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import hogent.hogentprojecteniii_groep10.R;
import hogent.hogentprojecteniii_groep10.helpers.HelperMethods;
import hogent.hogentprojecteniii_groep10.helpers.RestClient;
import hogent.hogentprojecteniii_groep10.main.Main;
import hogent.hogentprojecteniii_groep10.models.Gebruiker;
import hogent.hogentprojecteniii_groep10.models.Kind;
import hogent.hogentprojecteniii_groep10.models.Vacation;
import hogent.hogentprojecteniii_groep10.models.VacationResponse;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Een laatste overzicht die de gegevens toont die ingevuld zijn bij een inschrijving.
 */
public class VacationSignupOverviewActivity extends Activity {

    private Vacation selectedVacation;
    private Kind[] signedUpChildren;
    private String housenumberTxt, postalCodeTxt, streetTxt, cityTxt, firstnameTxt, nameTxt;
    private TextView signupOverviewVacationTitleLbl, signupOverviewVacationDateLbl,
            signupOverviewVacationPriceLbl, signupOverviewVacationTotalPriceLbl,
            signupOverviewBillingNameLbl, signupOverviewBillingAddressStreetHousenumberLbl,
            signupOverviewBillingAddressPostalcodeCityLbl;
    private LinearLayout signupOverviewSignupsLinearLayout;
    private Button cancelBtn, signupBtn;


    /**
     * Initialiseert de activity.
     * Zal alle gegevens uit de bundels halen die uit de vorige activites waren opgemaakt.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_signup_overview);

        selectedVacation = getIntent().getParcelableExtra("SpecificVacation");
        Parcelable[] parcelableArray = getIntent().getParcelableArrayExtra("SignedUpChildren");
        if (parcelableArray != null) {
            signedUpChildren = Arrays.copyOf(parcelableArray, parcelableArray.length, Kind[].class);
        }
        streetTxt = getIntent().getStringExtra("streetTxt");
        postalCodeTxt = getIntent().getStringExtra("postalCodeTxt");
        cityTxt = getIntent().getStringExtra("cityTxt");
        housenumberTxt = getIntent().getStringExtra("housenumberTxt");
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

        signupOverviewVacationTitleLbl.setText(selectedVacation.getTitle());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        signupOverviewVacationDateLbl.setText(String.format("%s - %s", formatter.format(selectedVacation.getBeginDate().getTime()), formatter.format(selectedVacation.getEndDate().getTime())));
        signupOverviewVacationPriceLbl.setText(String.format("€%.2f", selectedVacation.getBaseCost()));
        signupOverviewVacationTotalPriceLbl.setText(String.format("€%.2f", selectedVacation.getBaseCost() * signedUpChildren.length));
        signupOverviewBillingNameLbl.setText(firstnameTxt + " " + nameTxt);
        signupOverviewBillingAddressStreetHousenumberLbl.setText(streetTxt + " " + housenumberTxt);
        signupOverviewBillingAddressPostalcodeCityLbl.setText(postalCodeTxt + " " + cityTxt);

        for(Kind child : signedUpChildren){
            TextView childTextView = new TextView(getApplicationContext());
            childTextView.setText(child.getFirstName() + " " + child.getLastName());
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

                Map<String, String> registerValues = new HashMap<String, String>();

                registerValues.put("vacation_id", Long.toString(selectedVacation.getId()));
                registerValues.put("is_paid", Integer.toString(0)); //Niet betaald?
                registerValues.put("facturation_first_name", firstnameTxt);
                registerValues.put("facturation_last_name", nameTxt);
                registerValues.put("street_name", streetTxt);
                registerValues.put("postal_code", postalCodeTxt);
                registerValues.put("house_number", housenumberTxt);
                registerValues.put("city", cityTxt);


                Log.i("VacationSignupOverview", Arrays.toString(signedUpChildren));

                for(Kind k : signedUpChildren){
                    registerValues.put("child_id", Long.toString(k.getId()));
                    new RegisterChildTask(registerValues, k.getId()).execute();
                }


                Intent intent = new Intent(getApplicationContext(), Main.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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
        getMenuInflater().inflate(R.menu.vacation_signup_overview, menu);
        if( getActionBar() != null)
            getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        return true;
    }

    public class RegisterChildTask extends AsyncTask<Map<String, String>, Void, Void> {
        private Map<String, String> registerValues;
        private long childId;
        private RestClient restClient;

        public RegisterChildTask(Map<String, String> registerValues, long childId){
            this.registerValues = registerValues;
            this.childId = childId;

            SharedPreferences sharedPref = getApplicationContext()
                    .getSharedPreferences(getApplicationContext().getString(R.string.authorization_preference_file), Context.MODE_PRIVATE);
            String token = sharedPref.getString(getApplicationContext().getResources().getString(R.string.authorization), "No token");
            restClient = new RestClient(token);
        }

        @Override
        protected Void doInBackground(Map<String, String>... params) {

            Log.i("VacationSignupOverview", "Registering: " + childId);

            Callback<Response> callback = new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    Toast.makeText(getApplicationContext(), "Een inschrijving is gemaakt!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Er kon niet ingeschreven worden.", Toast.LENGTH_SHORT).show();
                }
            };
            restClient.getRestService().registerChild(registerValues, childId, callback);


            return null;
        }
    }
}
