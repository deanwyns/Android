package hogent.hogentprojecteniii_groep10.authentication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hogent.hogentprojecteniii_groep10.R;
import hogent.hogentprojecteniii_groep10.helpers.RestClient;
import hogent.hogentprojecteniii_groep10.interfaces.RestService;
import hogent.hogentprojecteniii_groep10.models.Gebruiker;
import hogent.hogentprojecteniii_groep10.models.Ouder;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Activity om een nieuwe account aan te maken als ouder
 */
public class Registreren extends Activity {

    private EditText mVoornaamMoederView, mNaamMoederView, mRrnMoederView, mNaamVaderView, mVoornaamVaderView,
            mRrnVaderView, mTelNrView, mEmailView, mPasswordView, mBevestigPasswordView;
    private Button mRegistrerenButton/* mCancelButton*/;
    private UserRegisterTask mAuthTask = null;
    private boolean isTelnrValid, isEmailValid, isPasswordValid, isVoornaamMoederValid, isNaamMoederValid,
    isRrnMoederValid, isBevestigPasswordValid, isNaamVaderValid=true, isVoornaamVaderValid=true,
            isRrnVaderValid=true;

    /**
     *  Initialiseert het scherm
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registreren);

        mVoornaamMoederView = (EditText) findViewById(R.id.voornaam_moeder);
        mNaamMoederView = (EditText) findViewById(R.id.naam_moeder);
        mRrnMoederView = (EditText) findViewById(R.id.rrn_moeder);
        mNaamVaderView = (EditText) findViewById(R.id.naam_vader);
        mVoornaamVaderView = (EditText) findViewById(R.id.voornaam_vader);
        mRrnVaderView = (EditText) findViewById(R.id.rrn_vader);
        mTelNrView = (EditText) findViewById(R.id.telNr);
        mEmailView = (EditText) findViewById(R.id.registreer_email);
        mPasswordView = (EditText) findViewById(R.id.registreer_password);
        mRegistrerenButton = (Button) findViewById(R.id.sign_up);
        mBevestigPasswordView = (EditText) findViewById(R.id.bevestig_password);

        setUpListeners();
    }

    /**
     * Voegt Listeners toe aan de knoppen en de EditText velden
     * en stelt het gedrag in van de enter knop op het android toetsenbord
     *
     */
    private void setUpListeners() {
        mRegistrerenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegistration();
            }
        });
        /**
         *  Als de inhoud van de tekstvelden ongeldig is doet de enterknop op het toetsenbord bij het laatste
         *  tekstveld niets anders zal de registratie gestart worden
        */
        mRrnVaderView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean isValidKey = keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER;
                boolean isValidAction = actionId == EditorInfo.IME_ACTION_DONE;

                if ((isValidAction || isValidKey)&& isTelnrValid && isEmailValid && isPasswordValid
                        && isVoornaamMoederValid && isNaamMoederValid && isRrnMoederValid){
                    attemptRegistration();
                }
                return false;
            }
        });
        /**
         * Valideert de inhoud van het tekstveld wanneer het verandert
         */
        mPasswordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i2, int i3) {
            }
            @Override
            public void onTextChanged(CharSequence s, int i, int i2, int i3) {
                isPasswordValid(s.toString());
                changeButtonState();
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        /**
         * Valideert de inhoud van het tekstveld wanneer het verandert
         */
        mVoornaamMoederView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i2, int i3) {
            }
            @Override
            public void onTextChanged(CharSequence s, int i, int i2, int i3) {
                isVoornaamMoederValid = s.length()!=0;
                changeButtonState();
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        /**
         * Valideert de inhoud van het tekstveld wanneer het verandert
         */
        mNaamMoederView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i2, int i3) {
            }
            @Override
            public void onTextChanged(CharSequence s, int i, int i2, int i3) {
                isNaamMoederValid = s.length()!=0;
                changeButtonState();
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        /**
         * Valideert de inhoud van het tekstveld wanneer het verandert
         */
        mRrnMoederView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i2, int i3) {
            }
            @Override
            public void onTextChanged(CharSequence s, int i, int i2, int i3) {
                isRrnMoederValid = isRrnValid(s.toString());
                changeButtonState();
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        /**
         * Valideert de inhoud van het tekstveld wanneer het verandert
         */
        mTelNrView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i2, int i3) {
            }
            @Override
            public void onTextChanged(CharSequence s, int i, int i2, int i3) {
                isTelNrValid(s.toString());
                changeButtonState();
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        /**
         * Valideert de inhoud van het tekstveld wanneer het verandert
         */
        mEmailView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i2, int i3) {
            }
            @Override
            public void onTextChanged(CharSequence s, int i, int i2, int i3) {
                isEmailValid(s.toString());
                changeButtonState();
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        /**
         * Valideert de inhoud van het tekstveld wanneer het verandert
         */
        mBevestigPasswordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i2, int i3) {

            }
            @Override
            public void onTextChanged(CharSequence s, int i, int i2, int i3) {
                passwordsMatch(mPasswordView.getText().toString(), s.toString());
                changeButtonState();
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        /**
         * Valideert de inhoud van het tekstveld wanneer het verandert
         * De gegevens van de vader zijn niet verplicht dus hebben die tekstvelden geen invloud op de
         * geldigheid, maar als 1 tekstveld van de vader ingevuld is moeten de rest ook ingevuld zijn
         */
        mNaamVaderView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i2, int i3) {
                if(!s.toString().isEmpty()) {
                    if (mVoornaamVaderView.getText().toString().isEmpty())
                        isVoornaamVaderValid = false;
                    isRrnVaderValid=isRrnValid(mRrnVaderView.getText().toString());
                }
                changeValidityGegevensVader();
                changeButtonState();

            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        /**
         * Valideert de inhoud van het tekstveld wanneer het verandert
         */
        mVoornaamVaderView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i2, int i3) {
                if(!s.toString().isEmpty()) {
                    isVoornaamVaderValid=true;
                    if (mNaamVaderView.getText().toString().isEmpty())
                        isNaamVaderValid = false;
                    isRrnVaderValid=isRrnValid(mRrnVaderView.getText().toString());
                }
                changeValidityGegevensVader();
                changeButtonState();


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        /**
         * Valideert de inhoud van het tekstveld wanneer het verandert
         */
        mRrnVaderView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i2, int i3) {
                if(!s.toString().isEmpty()) {
                    if (mVoornaamVaderView.getText().toString().isEmpty())
                        isVoornaamVaderValid = false;
                    if(mNaamVaderView.getText().toString().isEmpty())
                        isNaamVaderValid = false;
                    isRrnVaderValid=isRrnValid(mRrnVaderView.getText().toString());
                }
                changeValidityGegevensVader();
                changeButtonState();


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    /**
     *Wanneer geen van de tekstvelden van de vader ingevuld zijn worden de booleans ervoor op
     * true gezet zodat ze geen invloed hebben op het verdere verloop
     */
    public void changeValidityGegevensVader(){
        if (mNaamVaderView.getText().toString().isEmpty()
                && mVoornaamVaderView.getText().toString().isEmpty()
                && mRrnVaderView.getText().toString().isEmpty()) {
            isVoornaamVaderValid = true;
            isNaamVaderValid = true;
            isRrnVaderValid = true;
        }
    }

    /**
     *Maakt een ouder object aan met de gegevens uit de tekstvelden en geeft dit door
     * naar de asynchrone taak om te registreren
     */
    public void attemptRegistration() {

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String password2 = mBevestigPasswordView.getText().toString();
        String naamMoeder = mNaamMoederView.getText().toString();
        String voornaamMoeder = mVoornaamMoederView.getText().toString();
        String rrnMoeder = mRrnMoederView.getText().toString();
        String naamVader = mNaamVaderView.getText().toString();
        String voornaamVader = mVoornaamVaderView.getText().toString();
        String rrnVader = mRrnVaderView.getText().toString();
        String telNr = mTelNrView.getText().toString();

            Ouder ouder = new Ouder(email,password,telNr, naamMoeder,voornaamMoeder, rrnMoeder,  voornaamVader,naamVader, rrnVader);
            mAuthTask = new UserRegisterTask(ouder, password2);
            mAuthTask.execute((Void) null);

    }

    /**
     *Kijkt of het ingegeven telefoonnummer een geldig belgisch telefoonnummer is
     * @param telnr
     */
    private void isTelNrValid(String telnr){
        String telnrRegex;
        Pattern patternTelnr;
        telnrRegex = "0(\\d{3}|\\d{2})\\d{6}";
        patternTelnr = Pattern.compile(telnrRegex);
        Matcher matcher = patternTelnr.matcher(telnr);

        isTelnrValid =  matcher.find();
    }

    /**
     * Kijkt of het ingegeven emailadres een geldig emailadres is
     * @param email
     */
    private void isEmailValid(String email) {
        String emailRegEx;
        Pattern pattern;
        // Regex for a valid email address
        emailRegEx = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$";
        // Compare the regex with the email address
        pattern = Pattern.compile(emailRegEx);
        Matcher matcher = pattern.matcher(email);

        isEmailValid =  matcher.find() && !email.isEmpty();
    }

    /**
     *Kijkt of de tekst in het wachtwoord tekstveld overeenkomt met de tekst in het
     * bevestig wachtwoord tekstveld
     * @param pw1
     * @param pw2
     */
    private void passwordsMatch(String pw1, String pw2) {
        isBevestigPasswordValid =  pw1.equals(pw2);
    }

    /**
     * Kijkt of de tekst in het rrn tekstveld een geldig
     * rijksregisternummer is
     * @param rrn
     * @return
     */
    private boolean isRrnValid(String rrn){
        if (rrn.length()==11) {
            int rrnNumber = Integer.parseInt(rrn.substring(0,9));
            int modulo = rrnNumber % 97;
            int checkSum = Integer.parseInt(rrn.substring(9,11));
            if (Integer.compare(modulo, 97-checkSum)==0)
                return true;
            else
                return false;
        }else
            return false;
    }

    /**
     * Kijkt of de tekst in het wachtwoord tekstvenster overeenkomt met de opgelegde regels
     * @param password
     */
    private void isPasswordValid(String password) {
        isPasswordValid =  password.length()==6;
    }

    /**
     * Stelt het errorfield in van de tekstvelden afhankelijk van de geldigheid van de tekstvelden
     * verwijdert het veld wanneer de gegevens geldig zijn
     */
    public void setErrors(){
        if (isTelnrValid && isEmailValid && isPasswordValid
                && isVoornaamMoederValid && isNaamMoederValid && isRrnMoederValid
                && isBevestigPasswordValid && isVoornaamVaderValid && isNaamVaderValid && isRrnVaderValid) {
            mBevestigPasswordView.setError(null);
            mTelNrView.setError(null);
            mEmailView.setError(null);
            mPasswordView.setError(null);
            mVoornaamMoederView.setError(null);
            mNaamMoederView.setError(null);
            mRrnMoederView.setError(null);
            mVoornaamVaderView.setError(null);
            mNaamVaderView.setError(null);
            mRrnVaderView.setError(null);
        }else{
            if (!isTelnrValid)
                mTelNrView.setError(getString(R.string.error_invalid_telephonenumber));
            if(!isEmailValid)
                mEmailView.setError(getString(R.string.error_invalid_email));
            if(!isPasswordValid)
                mPasswordView.setError(getString(R.string.error_invalid_password));
            if (!isVoornaamMoederValid)
                mVoornaamMoederView.setError(getString(R.string.error_field_required));
            if(!isNaamMoederValid)
                mNaamMoederView.setError(getString(R.string.error_field_required));
            if(!isRrnMoederValid)
                mRrnMoederView.setError(getString(R.string.error_invalid_rrn));
            if(!isBevestigPasswordValid)
                mBevestigPasswordView.setError(getString(R.string.error_passwords_different));
            if(!isRrnVaderValid)
                mRrnVaderView.setError(getString(R.string.error_invalid_rrn));
            if(!isNaamVaderValid)
                mNaamVaderView.setError(getString(R.string.error_field_required));
            if(!isVoornaamVaderValid)
                mVoornaamVaderView.setError(getString(R.string.error_field_required));
        }

    }

    /**
     * Als alle velden correct gevalideerd zijn zal de knop om te registreren ge-enabled worden
     * anders zal de knop disabled worden
     */
    private void changeButtonState(){
        if (isTelnrValid && isEmailValid && isPasswordValid
                && isVoornaamMoederValid && isNaamMoederValid && isRrnMoederValid
                && isBevestigPasswordValid && isNaamVaderValid && isVoornaamVaderValid && isRrnVaderValid){
            mRegistrerenButton.setEnabled(true);
        }else{
            mRegistrerenButton.setEnabled(false);
        }
        setErrors();
    }

    /**
     * De asynchrone task om de registratie uit te voeren
     */
    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final Ouder mOuder;
        private final String passwordConfirmed;
        private ProgressDialog progressDialog;
        private RestClient restClient = new RestClient();

        /**
         *
         * @param ouder
         * @param passwordConfirmed
         */
        public UserRegisterTask(Ouder ouder, String passwordConfirmed) {
           this.mOuder=ouder;
           this.passwordConfirmed=passwordConfirmed;
        }

        /**
         * Voordat de task gestart wordt zal er een dialog getoond worden
         */
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(Registreren.this, getResources().getString(R.string.title_registreren), getResources().getString(R.string.please_wait), true);
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

            Map<String, String> signUpParamMap = new HashMap<String, String>();

            signUpParamMap.put("email", mOuder.getEmailadres());
            signUpParamMap.put("password", mOuder.getPassword());
            signUpParamMap.put("password_confirmed", passwordConfirmed);
            signUpParamMap.put("phone_number", mOuder.getTelNr());
            signUpParamMap.put("first_name_mother", mOuder.getVoornaam());
            signUpParamMap.put("last_name_mother", mOuder.getNaam());
            signUpParamMap.put("nrn_mother", mOuder.getRrnMoeder());
            signUpParamMap.put("first_name_father", mOuder.getVoornaamVader());
            signUpParamMap.put("last_name_father",  mOuder.getNaamVader());
            signUpParamMap.put("nrn_father", mOuder.getRrnVader());

            sendSignUpRequest(signUpParamMap);
            return true;
        }

        /**
         * Na de task zal het dialog verdwijnen, de asynchrone task gestopt worden
         * Als het registreren succesvol is zal de login activity gestart worden en
         * de huidige activity gestopt worden
         * @param success is true als het registreren succesvol is zoniet false
         */
        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            progressDialog.dismiss();
            if (success) {
                Intent loginIntent = new Intent(getApplicationContext(), Login.class);
                startActivity(loginIntent);
                finish();
            }

        }

        /**
         *  Zal het http request naar de server versturen en afhankelijk van of het registreren
         *  wel of niet succesvol was de succes functie of failure functie aanroepen
         * @param signupParamMap de map die de gegevens van de te registreren ouder bevat
         */
        private void sendSignUpRequest(Map<String, String> signupParamMap){

            Callback<String> gebruiker = new Callback<String>() {
                @Override
                public void success(String gebruiker, Response response) {
                    response.getBody();
                    Toast.makeText(getBaseContext(), "Geregistreerd", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    Toast.makeText(getBaseContext(), error.getResponse().toString(), Toast.LENGTH_SHORT).show();
                }

            };
            restClient.getRestService().register(signupParamMap, gebruiker);
        }

        /**
         * Wanneer er geannuleerd wordt wordt de asynchrone task geannuleerd en
         * zal het dialog verdwijnen
         */
        @Override
        protected void onCancelled() {
            mAuthTask = null;
            progressDialog.dismiss();
        }
    }
 }
