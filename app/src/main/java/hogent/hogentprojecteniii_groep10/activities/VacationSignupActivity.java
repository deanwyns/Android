package hogent.hogentprojecteniii_groep10.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import hogent.hogentprojecteniii_groep10.R;
import hogent.hogentprojecteniii_groep10.helpers.HelperMethods;
import hogent.hogentprojecteniii_groep10.helpers.RestClient;
import hogent.hogentprojecteniii_groep10.models.Address;
import hogent.hogentprojecteniii_groep10.models.Gebruiker;
import hogent.hogentprojecteniii_groep10.models.Kind;
import hogent.hogentprojecteniii_groep10.models.Vacation;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * De activity die zal toelaten om kinderen in te schrijven in een vakantie.
 */
public class VacationSignupActivity extends Activity {

    private Button addChildToAccountBtn, goToBillingBtn, addChildSpinnerBtn;
    private LinearLayout childrenSpinnerLayout;
    //private Spinner firstChildSpinner;
    private List<Spinner> spinnerList = new ArrayList<Spinner>();

    private Kind[] kinderen;
    private ArrayAdapter<Kind> adapter;
    private Vacation vacation;

    /**
     * Zal het scherm initialiseren
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_signup);

        kinderen = new Kind[0];
        vacation = (Vacation) getIntent().getParcelableExtra("SpecificVacation");

        addChildToAccountBtn = (Button) findViewById(R.id.add_child_to_account_btn);
        goToBillingBtn = (Button) findViewById(R.id.go_to_billing_btn);
        addChildSpinnerBtn = (Button) findViewById(R.id.add_child_spinner_btn);
        childrenSpinnerLayout = (LinearLayout) findViewById(R.id.linear_layout_children_spinners);
        //firstChildSpinner  = (Spinner) findViewById(R.id.first_child_spinner);
        //spinnerList.add(firstChildSpinner);

        new GetChildrenTask().execute();

        setUpListeners();
    }

    private void setupAdapter() {
        adapter = new ArrayAdapter<Kind>(getApplicationContext(), android.R.layout.simple_spinner_item, kinderen);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //firstChildSpinner.setAdapter(adapter);

        //Vervanging van spinner uit XML omdat je niet zomaar themes & styles kan toepassen in code
        Spinner childSpinner = new Spinner(getApplicationContext(), null, android.R.attr.spinnerStyle);
        spinnerList.add(childSpinner);
        childSpinner.setAdapter(adapter);
        childrenSpinnerLayout.addView(childSpinner);
    }

    /**
     * Zal listeners toevoegen op de knoppen.
     */
    private void setUpListeners() {
        addChildSpinnerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Spinner childSpinner = new Spinner(getApplicationContext());
                if(spinnerList.size() < kinderen.length){
                    Spinner childSpinner = new Spinner(getApplicationContext(), null, android.R.attr.spinnerStyle);
                    spinnerList.add(childSpinner);
                    childSpinner.setAdapter(adapter);
                    childrenSpinnerLayout.addView(childSpinner);
                }
            }
        });

        addChildToAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addChild = new Intent(getApplicationContext(), AddChildActivity.class);
                startActivity(addChild);
            }
        });

        goToBillingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!HelperMethods.isNetworkAvailable(getApplicationContext())){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet_available), Toast.LENGTH_SHORT).show();
                }else if(kinderen.length < 1){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_child_selected), Toast.LENGTH_SHORT).show();
                }else{
                    Intent billingDetails = new Intent(getApplicationContext(), VacationBillingActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putParcelable("SpecificVacation", vacation);
                    Kind[] signedUpChildren = new Kind[spinnerList.size()];
                    for (int i = 0; i < spinnerList.size(); i++){
                        signedUpChildren[i] = adapter.getItem(i);
                    }
                    mBundle.putParcelableArray("SignedUpChildren", signedUpChildren);
                    billingDetails.putExtras(mBundle);
                    startActivity(billingDetails);
                }
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
        getMenuInflater().inflate(R.menu.vacation_signup, menu);
        if( getActionBar() != null)
        {
            getActionBar().setTitle(getResources().getString(R.string.title_activity_vacation_signup) + " " + vacation.getTitle());
            getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        }

        return true;
    }

    public class GetChildrenTask extends AsyncTask<Void, Void, List<Kind>> {
        private RestClient restClient;

        public GetChildrenTask() {
            SharedPreferences sharedPref =
                    getApplication().
                            getSharedPreferences(getString(R.string.authorization_preference_file),
                                    Context.MODE_PRIVATE);
            String token = sharedPref.getString(getResources().getString(R.string.authorization), "No token");
            restClient = new RestClient(token);

        }

        @Override
        protected List<Kind> doInBackground(Void... voids) {
            return restClient.getRestService().getChildren();
        }

        @Override
        protected void onPostExecute(List<Kind> children) {
            kinderen = new Kind[children.size()];
            for (int i = 0; i < children.size(); i++){
                kinderen[i] = children.get(i);
            }

            //Temp tot back-end eindelijk werkt
            kinderen = new Kind[1];
            kinderen[0] = new Kind("De Mei", "Jan", "12365489", "Stad");

            setupAdapter();
        }
    }

}
