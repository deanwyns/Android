package hogent.hogentprojecteniii_groep10.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hogent.hogentprojecteniii_groep10.R;
import hogent.hogentprojecteniii_groep10.authentication.Login;
import hogent.hogentprojecteniii_groep10.fragments.VacationDetailFragment;
import hogent.hogentprojecteniii_groep10.helpers.RestClient;
import hogent.hogentprojecteniii_groep10.helpers.SessionRequestInterceptor;
import hogent.hogentprojecteniii_groep10.interfaces.RestService;
import hogent.hogentprojecteniii_groep10.main.Main;
import hogent.hogentprojecteniii_groep10.models.Address;
import hogent.hogentprojecteniii_groep10.models.Gebruiker;
import hogent.hogentprojecteniii_groep10.models.Kind;
import hogent.hogentprojecteniii_groep10.models.Vacation;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

/**
 * Activity om een kind toe te voegen aan een account
 */
public class AddChildActivity extends FragmentActivity {

    private EditText mNaamView, mVoornaamView, mRrnView, mStraatView,
            mHuisnummerView, mPostcodeView, mStadView, mGeboortedatumView;
    private Button mToevoegenButton;
    private SimpleDateFormat dateFormatter;
    private DatePickerDialog dobPickerDialog;
    private UserAddChildTask mAuthTask = null;
    private boolean isNaamValid, isVoornaamValid, isRrnValid, isHuisnummerValid, isZipCodeValid, isStraatValid, isStadValid;
    private Calendar newDate = Calendar.getInstance();

    /**
     * Initialiseert het scherm
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        mNaamView = (EditText) findViewById(R.id.lastname_add_child);
        mVoornaamView = (EditText) findViewById(R.id.firstname_add_child);
        mRrnView = (EditText) findViewById(R.id.rrn_add_child);
        mStraatView = (EditText) findViewById(R.id.straat_add_child);
        mHuisnummerView = (EditText) findViewById(R.id.huisnummer_add_child);
        mPostcodeView = (EditText) findViewById(R.id.postcode_add_child);
        mStadView = (EditText) findViewById(R.id.stad_add_child);
        mToevoegenButton = (Button) findViewById(R.id.toevoegen_btn_add_child);
        mGeboortedatumView = (EditText) findViewById(R.id.dob_add_child);

        setUpListeners();
        setDateTimeField();
    }
    /**
     * Voegt Listeners toe aan de knoppen en de EditText velden
     * en stelt het gedrag in van de enter knop op het android toetsenbord
     */
    private void setUpListeners() {
        mToevoegenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptAdd();
            }
        });
        /**
         *  Als de inhoud van de tekstvelden ongeldig is doet de enter knop op het toetsenbord bij het laatste
         *  tekstveld niets anders zal de registratie gestart worden
         */
        mStadView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean isValidKey = keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER;
                boolean isValidAction = actionId == EditorInfo.IME_ACTION_DONE;
                if ((isValidAction || isValidKey)&& isHuisnummerValid && isNaamValid && isVoornaamValid && isRrnValid && isStraatValid && isStadValid && isZipCodeValid){
                    attemptAdd();
                }
                return false;
            }
        });

        mGeboortedatumView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dobPickerDialog.show();
            }

        });
        /**
         * Valideert de inhoud van het tekstveld wanneer het verandert
         */
        mNaamView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i2, int i3) {
                isNaamValid = !s.toString().isEmpty();
                changeButtonState();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        /**
         * Valideert de inhoud van het tekstveld wanneer het verandert
         */
        mVoornaamView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i2, int i3) {
                isVoornaamValid = !s.toString().isEmpty();
                changeButtonState();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        /**
         * Valideert de inhoud van het tekstveld wanneer het verandert
         */
        mRrnView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i2, int i3) {
                isRrnValid(s.toString());
                changeButtonState();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        /**
         * Valideert de inhoud van het tekstveld wanneer het verandert
         */
        mPostcodeView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i2, int i3) {
                isZipCodeValid(s.toString());
                changeButtonState();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        /**
         * Valideert de inhoud van het tekstveld wanneer het verandert
         */
        mHuisnummerView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i2, int i3) {
                isHuisnummerValid = !s.toString().isEmpty();
                changeButtonState();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        /**
         * Valideert de inhoud van het tekstveld wanneer het verandert
         */
        mStadView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i2, int i3) {
                isStadValid = !s.toString().isEmpty();
                changeButtonState();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        /**
         * Valideert de inhoud van het tekstveld wanneer het verandert
         */
        mStraatView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i2, int i3) {
                isStraatValid = !s.toString().isEmpty();
                changeButtonState();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        dobPickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newDate.set(year, monthOfYear, dayOfMonth);
                mGeboortedatumView.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    /**
     *  Kijkt of  de tekst in het rrn tekstveld een geldig
     * rijksregisternummer is
     * @param rrn
     */
    private void isRrnValid(String rrn) {
        if (rrn.length() == 11) {
            int rrnNumber = Integer.parseInt(rrn.substring(0, 9));

            if (newDate.get(Calendar.YEAR)>=2000){
                rrnNumber = rrnNumber+2000000000;
            }
            int modulo = rrnNumber % 97;
            int checkSum = Integer.parseInt(rrn.substring(9, 11));
            isRrnValid = (Integer.compare(modulo, 97 - checkSum) == 0);
        }else
            isRrnValid=false;
    }

    /**
     * Kijkt of  de tekst in het postcode tekstveld een geldige
     * postcode is
     * @param zipCode
     */
    private void isZipCodeValid(String zipCode){

        String zipCoderegex;
        Pattern pattern;
        // Regex for a valid email address
        zipCoderegex = "[1-9]\\d{3}$";
        // Compare the regex with the email address
        pattern = Pattern.compile(zipCoderegex);
        Matcher matcher = pattern.matcher(zipCode);

        isZipCodeValid =  matcher.matches();
    }

    /**
     * Verandert de tekstkleur afhankelijk van de overeenstemmende booleans
     */
    private void setErrors(){
        if (isHuisnummerValid)
            mHuisnummerView.setTextColor(Color.rgb(80, 200, 120));
        else
            mHuisnummerView.setTextColor(Color.RED);

        if (isNaamValid)
            mNaamView.setTextColor(Color.rgb(80, 200, 120));
        else
            mNaamView.setTextColor(Color.RED);

        if (isVoornaamValid)
            mVoornaamView.setTextColor(Color.rgb(80, 200, 120));
        else
            mVoornaamView.setTextColor(Color.RED);

        if (isRrnValid)
            mRrnView.setTextColor(Color.rgb(80, 200, 120));
        else
            mRrnView.setTextColor(Color.RED);

        if (isZipCodeValid)
            mPostcodeView.setTextColor(Color.rgb(80, 200, 120));
        else
            mPostcodeView.setTextColor(Color.RED);

        if (isStadValid)
            mStadView.setTextColor(Color.rgb(80, 200, 120));
        else
            mStadView.setTextColor(Color.RED);

        if (isStraatValid)
            mStraatView.setTextColor(Color.rgb(80, 200, 120));
        else
            mStraatView.setTextColor(Color.RED);
    }

    /**
     * Als alle velden correct gevalideerd zijn zal de knop om het kind toe te voegen
     * ge-enabled worden anders zal de knop disabled blijven
     */
    private void changeButtonState(){
        if (isHuisnummerValid && isNaamValid && isVoornaamValid && isRrnValid && isStraatValid && isStadValid && isZipCodeValid){
            mToevoegenButton.setEnabled(true);
            setErrors();
        }else {
            mToevoegenButton.setEnabled(false);
            setErrors();
        }
    }
    /**
     *Maakt een kind object aan met de gegevens uit de tekstvelden en geeft dit door
     * naar de asynchrone taak om het kind teo te voegen
     */
    public void attemptAdd() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String naam = mNaamView.getText().toString();
        String voornaam = mVoornaamView.getText().toString();
        String rrn = mRrnView.getText().toString();
        String straat = mStraatView.getText().toString();
        String huisnummer = mHuisnummerView.getText().toString();
        String postcode = mPostcodeView.getText().toString();
        String stad = mStadView.getText().toString();
        String dateOfBirth = mGeboortedatumView.getText().toString();


        Kind child = new Kind(naam, voornaam, rrn, straat, huisnummer, stad, postcode, dateOfBirth);

        mAuthTask = new UserAddChildTask(child);
        mAuthTask.execute((Void) null);

    }

    /**
     * De asynchrone task om het kind toe te voegen
     */
    public class UserAddChildTask extends AsyncTask<Void, Void, Boolean> {

        private final Kind mKind;
        private RestClient restClient;
        private ProgressDialog progressDialog;

        /**
         * Constructor van de asynchrone task. Haalt hier het token op van de ingelogde gebruiker
         * om mee te kunnen geven in de header van het http request
         * @param kind
         */
        public UserAddChildTask(Kind kind) {
            this.mKind =kind;

            SharedPreferences sharedPref =
                    getApplication().
                            getSharedPreferences(getString(R.string.authorization_preference_file),
                                    Context.MODE_PRIVATE);
            String token = sharedPref.getString(getResources().getString(R.string.authorization), "No token");
            restClient = new RestClient(token);

        }
        /**
         * Voordat de task gestart wordt zal er een dialog getoond worden
         */
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(AddChildActivity.this, getResources().getString(R.string.title_activity_add_child), getResources().getString(R.string.please_wait), true);
            super.onPreExecute();
        }

        /**
         * De parameter map die meegegeven zal worden met het HTTP request naar de server
         * zal hier opgevuld worden en meegegeven worden naar de functie die het
         * request zal versturen
         * @param voids
         * @return
         */
        @Override
        protected Boolean doInBackground(Void... voids) {
            Map<String, String> addChildParamMap = new HashMap<String, String>();



            addChildParamMap.put("first_name", mKind.getFirstName());
            addChildParamMap.put("last_name", mKind.getLastName());
            addChildParamMap.put("city", mKind.getCity());
            addChildParamMap.put("nrn", mKind.getNrn());
            addChildParamMap.put("street_name",mKind.getStreetName());
            addChildParamMap.put("house_number", mKind.getHouseNumber());
            addChildParamMap.put("city", mKind.getCity());
            addChildParamMap.put("postal_code", mKind.getPostalCode());
            addChildParamMap.put("date_of_birth", mKind.getDateOfBirth());


            sendAddChildRequest(addChildParamMap);
            return true;
        }

        /**
         * Na de task zal het dialog verdwijnen, de asynchrone task gestopt worden
         * Als het kind toevoegen succesvol is zal naar de parent activity gegaan worden en
         * de huidige activity gestopt worden
         * @param success is true als het het kind toevoegen succesvol is zoniet false
         */
        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            progressDialog.dismiss();

            if (success) {
                finish();}

        }

        /**
         * Zal het http request naar de server versturen en afhankelijk van of het kind toegevoegd
         *  is of niet de succes functie of failure functie aanroepen
         * @param addChildParamMap
         */
        private void sendAddChildRequest(Map <String, String> addChildParamMap){

                    Callback<String> kind = new Callback<String>() {
                        @Override
                        public void success(String kind, Response response) {
                            response.getBody();
                            Toast.makeText(getBaseContext(), "Toegevoegd", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            error.printStackTrace();
                            Log.i("AddChildActivity", mGeboortedatumView.getText().toString());
                        }

                    };

                    restClient.getRestService().addChild(addChildParamMap, kind);


        }
        /**
         * Wanneer er geannuleerd wordt wordt de asynchrone task geannuleerd en
         * zal het dialog verdwijnen
         */
        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}
