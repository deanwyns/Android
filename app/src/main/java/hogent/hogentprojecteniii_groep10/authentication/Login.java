package hogent.hogentprojecteniii_groep10.authentication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
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
import hogent.hogentprojecteniii_groep10.helpers.HelperMethods;
import hogent.hogentprojecteniii_groep10.helpers.RestClient;
import hogent.hogentprojecteniii_groep10.models.LoginToken;
import retrofit.RetrofitError;

/**
 * Activity om in te loggen
 */
public class Login extends Activity {
    private final static String TAG = "Login";
    private final static String GRANT_TYPE = "password";
    private static final String CLIENT_ID = "SPVS3YCK8QEbSXwneK4PFJrVPTrvM7ag9Yx2yrZC";
    private static final String CLIENT_SECRET = "vM&PR2rqMU8xD^&g9YUfJNbQj%ahqc_tTtNuaXAj";

    private EditText mEmailView;
    private EditText mPasswordView;
    private Button mEmailSignInButton;
    private UserLoginTask mAuthTask = null;
    private boolean emailValid;
    private boolean passwordValid;

    /**
     * Initialiseert het scherm
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email_address);
        mPasswordView = (EditText) findViewById(R.id.passWord);
        mEmailSignInButton = (Button) findViewById(R.id.btnLogIn);

        mEmailSignInButton.setEnabled(false);

        setUpListeners();
    }

    /**
     * Voegt Listeners toe aan de knoppen en de EditText velden
     * en stelt het gedrag in van de enter knop op het android toetsenbord
     */
    private void setUpListeners() {
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoginClicked();
            }
        });

        /**
         *  Als de inhoud van de tekstvelden ongeldig is doet de enter knop op het toetsenbord bij het laatste
         *  tekstveld niets anders zal de registratie gestart worden
         */
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean isValidKey = keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER;
                boolean isValidAction = actionId == EditorInfo.IME_ACTION_DONE;

                if ((isValidAction || isValidKey)&& emailValid && passwordValid){
                    attemptLogin();
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
                if (emailValid)
                    mEmailView.setError(null);
            }
        });
    }

    /**
     * Gaat naar de registreren activity
     */
    private void onSignUpClicked() {
        Intent registrerenIntent = new Intent(getApplicationContext(), Registreren.class);
        startActivity(registrerenIntent);
    }

    /**
     * Kijkt of het netwerk beschikbaar is en als er internetverbinding is zal de poging om in
     * te loggen starten
     */
    private void onLoginClicked() {
        if (!HelperMethods.isNetworkAvailable(getApplicationContext())) {
            Toast.makeText(getBaseContext(), "No network connection", Toast.LENGTH_SHORT).show();
        } else {
            attemptLogin();
        }
    }

    /**
     * Doet een login poging met de ingegeven waarden van het formulier
     */
    public void attemptLogin() {

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);

    }
    /**
     * Kijkt of de tekst een geldig emailadres is
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

        emailValid =  matcher.find();
    }
    /**
     * Kijkt of de tekst overeenkomt met de opgelegde regels voor wachtwoord
     * @param password
     */
    private void isPasswordValid(String password) {
        passwordValid =  !TextUtils.isEmpty(password);
    }
    /**
     * Als alle velden correct gevalideerd zijn zal de knop om te registreren ge-enabled worden
     * anders zal de knop disabled worden. Bij de ongeldige velden komt ook een errorfield te staan
     */
    private void changeButtonState(){
        if (emailValid && passwordValid){
            mEmailSignInButton.setEnabled(true);

        }else{
            mEmailSignInButton.setEnabled(false);
            if (!emailValid)
            mEmailView.setError(getString(R.string.error_invalid_email));
            if (!passwordValid)
            mPasswordView.setError(getString(R.string.error_field_required));
        }
    }

    /**
     * Stelt de asynchrone task voor om in te kunnen loggen
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private ProgressDialog progressDialog;
        private RestClient restClient = new RestClient();

        /**
         *
         * @param email
         * @param password
         */
        public UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;

        }
        /**
         * Voordat de task gestart wordt zal er een dialog getoond worden
         */
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(Login.this, getResources().getString(R.string.title_login), getResources().getString(R.string.please_wait), true);
            super.onPreExecute();
        }
        /**
         * De parameter map die meegegeven zal worden met het HTTP request naar de server
         * zal hier opgevuld worden en meegegeven worden naar de functie die het
         * request zal versturen
         * @param params
         * @return
         */
        @Override
        protected Boolean doInBackground(Void... params) {

            Map<String, String> loginParameterMap = new HashMap<String, String>();

            loginParameterMap.put("grant_type", GRANT_TYPE);
            loginParameterMap.put("username", mEmail);
            loginParameterMap.put("password", mPassword);
            loginParameterMap.put("client_id", CLIENT_ID);
            loginParameterMap.put("client_secret", CLIENT_SECRET);

            return sendLoginRequest(loginParameterMap);

        }
        /**
         *  Zal het http request naar de server versturen en afhankelijk van of het inloggen
         *  wel of niet succesvol was zal het logintoken dat van de server komt
         *  opgeslaan worden in de sharedpreferences
         * @param loginParameterMap de map die de gegevens van de in te loggen gebruiker bevat
         */
        private boolean sendLoginRequest(final Map<String, String> loginParameterMap) {

            LoginToken loginToken;
            try {
                loginToken = restClient.getRestService().login(loginParameterMap);
                if (loginToken != null) {
                    SharedPreferences sharedPref = getApplication()
                            .getSharedPreferences(getString(R.string.authorization_preference_file), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(getString(R.string.authorization), loginToken.toString());
                    editor.apply();
                    return true;
                }
            } catch (RetrofitError retrofitError) {
                retrofitError.printStackTrace();
                return false;
            }
            return false;
        }
        /**
         * Na de task zal het dialog verdwijnen, de asynchrone task gestopt worden
         * Als het inloggen succesvol is zal naar de parent activity teruggegaan worden
         * anders zal een errorfield ingesteld worden
         * @param success is true als het inloggen succesvol is zoniet false
         */
        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            progressDialog.dismiss();
            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
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
