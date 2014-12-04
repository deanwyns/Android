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
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Fabrice on 6/11/2014.
 */
public class Registreren extends Activity {

    private EditText mVoornaamMoederView, mNaamMoederView, mRrnMoederView, mNaamVaderView, mVoornaamVaderView,
            mRrnVaderView, mTelNrView, mEmailView, mPasswordView, mBevestigPasswordView;
    private Button mRegistrerenButton/* mCancelButton*/;
    private UserRegisterTask mAuthTask = null;
    private boolean isTelnrValid, isEmailValid, isPasswordValid, isVoornaamMoederValid, isNaamMoederValid,
    isRrnMoederValid, isBevestigPasswordValid, isNaamVaderValid=true, isVoornaamVaderValid=true,
            isRrnVaderValid=true;
    private View mProgressView;
    private View mRegisterFormView;

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
        mRegisterFormView = findViewById(R.id.register_form);
        mProgressView = findViewById(R.id.register_progress);
        //mCancelButton = (Button)findViewById(R.id.btnCancel_register);

        setUpListeners();
    }

    private void setUpListeners() {
        mRegistrerenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegistration();
            }
        });

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

    public void changeValidityGegevensVader(){
        if (mNaamVaderView.getText().toString().isEmpty()
                && mVoornaamVaderView.getText().toString().isEmpty()
                && mRrnVaderView.getText().toString().isEmpty()) {
            isVoornaamVaderValid = true;
            isNaamVaderValid = true;
            isRrnVaderValid = true;
        }
    }

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
        showProgress(true);

            Gebruiker gebruiker = new Gebruiker(email,password,telNr, naamMoeder,voornaamMoeder, rrnMoeder,  voornaamVader,naamVader, rrnVader);
            mAuthTask = new UserRegisterTask(gebruiker, password2);
            mAuthTask.execute((Void) null);

    }

    private void isTelNrValid(String telnr){
        String telnrRegex;
        Pattern patternTelnr;
        telnrRegex = "0(\\d{3}|\\d{2})\\d{6}";
        patternTelnr = Pattern.compile(telnrRegex);
        Matcher matcher = patternTelnr.matcher(telnr);

        isTelnrValid =  matcher.find();
    }

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

    private void passwordsMatch(String pw1, String pw2) {
        isBevestigPasswordValid =  pw1.equals(pw2);
    }

    private boolean isRrnValid(String rrn){
        if (rrn.length()==11) {
            return true;
            /*int rrnNumber = Integer.parseInt(rrn.substring(0, 8));
            int modulo = rrnNumber % 97;

            return ((97 - modulo)-Integer.parseInt(rrn.substring(9)))==0;*/
        }else
            return false;
    }

    private void isPasswordValid(String password) {
        isPasswordValid =  !password.isEmpty();
    }

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

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            //mCancelButton.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
           // mCancelButton.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final Gebruiker mGebruiker;
        private final String passwordConfirmed;
        private ProgressDialog progressDialog;
        private RestClient restClient = new RestClient();

        public UserRegisterTask(Gebruiker gebruiker, String passwordConfirmed) {
           this.mGebruiker=gebruiker;
           this.passwordConfirmed=passwordConfirmed;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(Registreren.this, getResources().getString(R.string.title_registreren), getResources().getString(R.string.please_wait), true);
            super.onPreExecute();
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
            //showProgress(false);
            progressDialog.dismiss();
            if (success) {
                Intent loginIntent = new Intent(getApplicationContext(), Login.class);
                startActivity(loginIntent);
                finish();
            }

        }

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

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            progressDialog.dismiss();
            //showProgress(false);
        }
    }
 }
