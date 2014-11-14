package hogent.hogentprojecteniii_groep10.authentication;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import hogent.hogentprojecteniii_groep10.R;
import hogent.hogentprojecteniii_groep10.interfaces.RestService;
import hogent.hogentprojecteniii_groep10.models.Gebruiker;
import hogent.hogentprojecteniii_groep10.models.Ouder;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

/**
 * Created by Fabrice on 6/11/2014.
 */
public class Registreren extends Activity {


    private EditText mVoornaamMoederView, mNaamMoederView, mRrnMoederView, mNaamVaderView, mVoornaamVaderView,
            mRrnVaderView, mTelNrView, mEmailView, mPasswordView, mBevestigPasswordView;
    private Button mRegistrerenButton;
    private UserRegisterTask mAuthTask = null;
    private final static String TAG = "Register";


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

    private void setUpListeners() {
        mRegistrerenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegistration();
            }
        });

        mBevestigPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    attemptRegistration();
                    handled = true;
                }
                return handled;
            }
        });
    }

    public void attemptRegistration() {
        resetErrors();

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

        boolean cancel = false;
        View focusView = null;

        //TODO: validatie nog aanpassen

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        if (!isPasswordValid(password)){
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (!passwordsMatch(password, password2)){
            mPasswordView.setError(getString(R.string.error_passwords_different));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(password2)) {
            mBevestigPasswordView.setError(getString(R.string.error_field_required));
            focusView = mBevestigPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(voornaamMoeder)) {
            mVoornaamMoederView.setError(getString(R.string.error_field_required));
            focusView = mVoornaamMoederView;
            cancel = true;
        }

        if (TextUtils.isEmpty(rrnMoeder)) {
            mRrnMoederView.setError(getString(R.string.error_field_required));
            focusView = mRrnMoederView;
            cancel = true;
        }

        if (TextUtils.isEmpty(telNr)) {
            mTelNrView.setError(getString(R.string.error_field_required));
            focusView = mTelNrView;
            cancel = true;
        }
        if (TextUtils.isEmpty(naamMoeder)) {
            mNaamMoederView.setError(getString(R.string.error_field_required));
            focusView = mNaamMoederView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            Gebruiker gebruiker = new Gebruiker(email,password,telNr, naamMoeder,voornaamMoeder, rrnMoeder,  voornaamVader,naamVader, rrnVader);
            mAuthTask = new UserRegisterTask(gebruiker, password2);
            mAuthTask.execute((Void) null);
        }

    }

    public void resetErrors() {
        mVoornaamMoederView.setError(null);
        mTelNrView.setError(null);
        mNaamMoederView.setError(null);
        mRrnMoederView.setError(null);
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mBevestigPasswordView.setError(null);
    }

    private boolean isEmailValid(String email) {
        String emailRegEx;
        Pattern pattern;
        // Regex for a valid email address
        emailRegEx = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$";
        // Compare the regex with the email address
        pattern = Pattern.compile(emailRegEx);
        Matcher matcher = pattern.matcher(email);

        return matcher.find();
    }

    private boolean passwordsMatch(String pw1, String pw2) {
        return pw1.equals(pw2);
    }

    private boolean isPasswordValid(String password) {
        //TODO:validatie wachtwoord toevoegen
        return true;
    }

    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final Gebruiker mGebruiker;
        private final String passwordConfirmed;

        public UserRegisterTask(Gebruiker gebruiker, String passwordConfirmed) {
           this.mGebruiker=gebruiker;
            this.passwordConfirmed=passwordConfirmed;

        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            Map<String, String> signUpParamMap = new HashMap<String, String>();

            signUpParamMap.put("email", mGebruiker.getEmailadres());
            signUpParamMap.put("password", mGebruiker.getPassword());
            signUpParamMap.put("password_confirmed", passwordConfirmed);
            signUpParamMap.put("phone_number", mGebruiker.getTelNr());
            signUpParamMap.put("first_name_mother", mGebruiker.getVoornaam());
            signUpParamMap.put("last_name_mother", mGebruiker.getNaam());
            signUpParamMap.put("nrn_mother", mGebruiker.getRrnMoeder());
            signUpParamMap.put("first_name_father", mGebruiker.getVoornaamOuder2());
            signUpParamMap.put("last_name_father",  mGebruiker.getNaamOuder2());
            signUpParamMap.put("nrn_father", mGebruiker.getRrnVader());

            sendSignUpRequest(signUpParamMap);
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (success) {
                Intent loginIntent = new Intent(getApplicationContext(), Login.class);
                startActivity(loginIntent);
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        private void sendSignUpRequest(Map<String, String> signupParamMap){

            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://lloyd.deanwyns.me/api")
                    .setConverter(new GsonConverter(gson))
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();
            RestService service = restAdapter.create(RestService.class);

            Callback<String> gebruiker = new Callback<String>() {
                @Override
                public void success(String gebruiker, Response response) {
                    //Log.i(TAG, gebruiker.toString());
                    response.getBody();
                    Toast.makeText(getBaseContext(), "Geregistreerd", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    error.getCause();
                }

            };
            service.register(signupParamMap, gebruiker);
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
 }
