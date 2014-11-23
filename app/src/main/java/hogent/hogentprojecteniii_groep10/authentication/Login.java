package hogent.hogentprojecteniii_groep10.authentication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
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
import hogent.hogentprojecteniii_groep10.helpers.NetworkingMethods;
import hogent.hogentprojecteniii_groep10.interfaces.RestService;
import hogent.hogentprojecteniii_groep10.models.LoginToken;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

public class Login extends Activity {
    private final static String TAG = "Login";
    private final static String GRANT_TYPE = "password";
    private static final String CLIENT_ID = "SPVS3YCK8QEbSXwneK4PFJrVPTrvM7ag9Yx2yrZC";
    private static final String CLIENT_SECRET = "vM&PR2rqMU8xD^&g9YUfJNbQj%ahqc_tTtNuaXAj";

    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private Button mEmailSignInButton, mSignUpButton, mCancelButton;
    private UserLoginTask mAuthTask = null;
    private boolean emailValid;
    private boolean passwordValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email_address);
        mPasswordView = (EditText) findViewById(R.id.passWord);
        mEmailSignInButton = (Button) findViewById(R.id.btnLogIn);
        mSignUpButton = (Button) findViewById(R.id.btnSignUp);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mCancelButton = (Button) findViewById(R.id.btnCancel_login);
        mEmailSignInButton.setEnabled(false);

        setUpListeners();
    }

    private void setUpListeners() {
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoginClicked();
            }
        });

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSignUpClicked();
            }
        });

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

    private void onSignUpClicked() {
        Intent registrerenIntent = new Intent(getApplicationContext(), Registreren.class);
        startActivity(registrerenIntent);
    }

    private void onLoginClicked() {
        if (!NetworkingMethods.isNetworkAvailable(getApplicationContext())) {
            Toast.makeText(getBaseContext(), "No network connection", Toast.LENGTH_SHORT).show();
        } else {
            attemptLogin();
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

            //mProgressView.setVisibility(View.VISIBLE);
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);

    }

    private void isEmailValid(String email) {
        String emailRegEx;
        Pattern pattern;
        // Regex for a valid email address
        emailRegEx = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$";
        // Compare the regex with the email address
        pattern = Pattern.compile(emailRegEx);
        Matcher matcher = pattern.matcher(email);

        emailValid =  matcher.find() && !TextUtils.isEmpty(email);
    }

    private void isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        passwordValid =  !TextUtils.isEmpty(password);
    }

    private void changeButtonState(){
        if (emailValid && passwordValid){
            mEmailSignInButton.setEnabled(true);
            mEmailView.setError(null);
            mPasswordView.setError(null);
        }else{
            mEmailSignInButton.setEnabled(false);
            if (!emailValid)
            mEmailView.setError(getString(R.string.error_invalid_email));
            if (!passwordValid)
            mPasswordView.setError(getString(R.string.error_field_required));
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mCancelButton.setVisibility(show ? View.VISIBLE : View.GONE);
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
            mCancelButton.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        public UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }


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

        private boolean sendLoginRequest(final Map<String, String> loginParameterMap) {

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://lloyd.deanwyns.me/api")
                    .build();
            RestService service = restAdapter.create(RestService.class);
            LoginToken loginToken;
            try {
                loginToken = service.login(loginParameterMap);
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

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            //showDialog();
            //mProgressView.setVisibility(View.INVISIBLE);
            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
            //showDialog();
            //mProgressView.setVisibility(View.INVISIBLE);
        }
    }
}
