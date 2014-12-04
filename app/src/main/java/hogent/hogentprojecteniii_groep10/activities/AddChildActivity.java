package hogent.hogentprojecteniii_groep10.activities;

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
import hogent.hogentprojecteniii_groep10.authentication.Login;
import hogent.hogentprojecteniii_groep10.fragments.VacationDetailFragment;
import hogent.hogentprojecteniii_groep10.helpers.RestClient;
import hogent.hogentprojecteniii_groep10.helpers.SessionRequestInterceptor;
import hogent.hogentprojecteniii_groep10.interfaces.RestService;
import hogent.hogentprojecteniii_groep10.main.Main;
import hogent.hogentprojecteniii_groep10.models.Gebruiker;
import hogent.hogentprojecteniii_groep10.models.Kind;
import hogent.hogentprojecteniii_groep10.models.Vacation;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;


public class AddChildActivity extends FragmentActivity {

    private EditText mNaamView, mVoornaamView, mRrnView, mStraatView, mHuisnummerView, mPostcodeView, mStadView;
    private Button mToevoegenButton;
    private UserAddChildTask mAuthTask = null;
    private boolean isNaamValid, isVoornaamValid, isRrnValid, isHuisnummerValid, isZipCodeValid, isStraatValid, isStadValid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);

        mNaamView = (EditText) findViewById(R.id.lastname_add_child);
        mVoornaamView = (EditText) findViewById(R.id.firstname_add_child);
        mRrnView = (EditText) findViewById(R.id.rrn_add_child);
        mStraatView = (EditText) findViewById(R.id.straat_add_child);
        mHuisnummerView = (EditText) findViewById(R.id.huisnummer_add_child);
        mPostcodeView = (EditText) findViewById(R.id.postcode_add_child);
        mStadView = (EditText) findViewById(R.id.stad_add_child);
        mToevoegenButton = (Button) findViewById(R.id.toevoegen_btn_add_child);

        setUpListeners();
    }

    private void setUpListeners() {
        mToevoegenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptAdd();
            }
        });

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

    private void isRrnValid(String rrn){
        if (rrn.length()==11) {
            isRrnValid = true;
            /*int rrnNumber = Integer.parseInt(rrn.substring(0, 8));
            int modulo = rrnNumber % 97;

            return ((97 - modulo)-Integer.parseInt(rrn.substring(9)))==0;*/
        }else
            isRrnValid =  false;

    }

    private void isZipCodeValid(String zipCode){
        String zipCoderegex;
        Pattern pattern;
        // Regex for a valid email address
        zipCoderegex = "[1-9]\\d{3}$";
        // Compare the regex with the email address
        pattern = Pattern.compile(zipCoderegex);
        Matcher matcher = pattern.matcher(zipCode);

        isZipCodeValid =  matcher.find();
    }

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

    private void changeButtonState(){
        if (isHuisnummerValid && isNaamValid && isVoornaamValid && isRrnValid && isStraatValid && isStadValid && isZipCodeValid){
            mToevoegenButton.setEnabled(true);
            setErrors();
        }else {
            mToevoegenButton.setEnabled(false);
            setErrors();
        }
    }

    public void attemptAdd() {

        String naam = mNaamView.getText().toString();
        String voornaam = mVoornaamView.getText().toString();
        String rrn = mRrnView.getText().toString();
        String straat = mStraatView.getText().toString();
        String huisnummer = mHuisnummerView.getText().toString();
        String postcode = mPostcodeView.getText().toString();
        String stad = mStadView.getText().toString();

        Kind child = new Kind(naam, voornaam, rrn, straat, huisnummer,stad, postcode);

        mAuthTask = new UserAddChildTask(child);
        mAuthTask.execute((Void) null);

    }

    public class UserAddChildTask extends AsyncTask<Void, Void, Boolean> {

        private final Kind mKind;
        private RestClient restClient;
        private ProgressDialog progressDialog;

        public UserAddChildTask(Kind kind) {
            this.mKind =kind;

            SharedPreferences sharedPref =
                    getApplication().
                            getSharedPreferences(getString(R.string.authorization_preference_file),
                                    Context.MODE_PRIVATE);
            String token = sharedPref.getString(getResources().getString(R.string.authorization), "No token");
            restClient = new RestClient(token);

        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(AddChildActivity.this, getResources().getString(R.string.title_login), getResources().getString(R.string.please_wait), true);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            Map<String, String> addChildParamMap = new HashMap<String, String>();

            addChildParamMap.put("firstName", mKind.getFirstName());
            addChildParamMap.put("lastName", mKind.getLastName());
            addChildParamMap.put("streetName", mKind.getStreetName());
            addChildParamMap.put("houseNumber", mKind.getHouseNumber());
            addChildParamMap.put("city", mKind.getCity());
            addChildParamMap.put("postalCode", mKind.getPostalCode());
            addChildParamMap.put("nrn", mKind.getNrn());

            sendAddChildRequest(addChildParamMap);
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            progressDialog.dismiss();

            if (success) {
                /*Intent vacationSignUp = new Intent(getApplicationContext(), VacationSignupActivity.class);
                startActivity(vacationSignUp);*/
                finish();
            }
            else {
                Intent vacationSignUp = new Intent(getApplicationContext(), Main.class);
                startActivity(vacationSignUp);
                finish();
            }
        }

        private void sendAddChildRequest(Map <String, String> addChildParamMap){

            Callback<String> kind = new Callback<String>() {
                @Override
                public void success(String kind, Response response) {
                    //Log.i(TAG, kind.toString());
                    response.getBody();
                    Toast.makeText(getBaseContext(), "Toegevoegd", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void failure(RetrofitError error) {

                }

            };
            restClient.getRestService().addChild(addChildParamMap, kind);
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}
