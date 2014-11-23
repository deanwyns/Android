package hogent.hogentprojecteniii_groep10.activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
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
import hogent.hogentprojecteniii_groep10.interfaces.RestService;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_child);

        mNaamView = (EditText) findViewById(R.id.lastname_add_child);
        mVoornaamView = (EditText) findViewById(R.id.firstname_add_child);
        mRrnView = (EditText) findViewById(R.id.rrn_add_child);
        mStraatView = (EditText) findViewById(R.id.naam_vader);
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

    }

    public void attemptAdd() {
        resetErrors();

        String naam = mNaamView.getText().toString();
        String voornaam = mVoornaamView.getText().toString();
        String rrn = mRrnView.getText().toString();
        String straat = mStraatView.getText().toString();
        String huisnummer = mHuisnummerView.getText().toString();
        String postcode = mPostcodeView.getText().toString();
        String stad = mStadView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(naam)) {
            mNaamView.setError(getString(R.string.error_field_required));
            focusView = mNaamView;
            cancel = true;
        }

        if (TextUtils.isEmpty(voornaam)) {
            mVoornaamView.setError(getString(R.string.error_invalid_email));
            focusView = mVoornaamView;
            cancel = true;
        }

        if (TextUtils.isEmpty(rrn)) {
            mRrnView.setError(getString(R.string.error_field_required));
            focusView = mRrnView;
            cancel = true;
        }

        if (TextUtils.isEmpty(straat)){
            mStraatView.setError(getString(R.string.error_invalid_password));
            focusView = mStraatView;
            cancel = true;
        }

        if (TextUtils.isEmpty(huisnummer)) {
            mHuisnummerView.setError(getString(R.string.error_field_required));
            focusView = mHuisnummerView;
            cancel = true;
        }

        if (TextUtils.isEmpty(postcode)) {
            mPostcodeView.setError(getString(R.string.error_field_required));
            focusView = mPostcodeView;
            cancel = true;
        }

        if (TextUtils.isEmpty(stad)) {
            mStadView.setError(getString(R.string.error_field_required));
            focusView = mStadView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            Kind kind = new Kind(naam,voornaam,rrn, straat,huisnummer, stad, postcode);
            mAuthTask = new UserAddChildTask(kind);
            mAuthTask.execute((Void) null);
        }

    }

    public void resetErrors() {
        mNaamView.setError(null);
        mVoornaamView.setError(null);
        mRrnView.setError(null);
        mStadView.setError(null);
        mStraatView.setError(null);
        mHuisnummerView.setError(null);
        mPostcodeView.setError(null);
    }

    public class UserAddChildTask extends AsyncTask<Void, Void, Boolean> {

        private final Kind mKind;

        public UserAddChildTask(Kind kind) {
            this.mKind =kind;

        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            Map<String, String> addChildParamMap = new HashMap<String, String>();
            addChildParamMap.put("firstName", mKind.getVoornaam());
            addChildParamMap.put("lastName", mKind.getNaam());
            addChildParamMap.put("streetName", mKind.getStraat());
            addChildParamMap.put("houseNumber", mKind.getHuisnummer());
            addChildParamMap.put("city", mKind.getStad());
            addChildParamMap.put("nrn", mKind.getRrn());

            sendAddChildRequest(addChildParamMap);
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (success) {
                Intent vacationSignUp = new Intent(getApplicationContext(), VacationSignupActivity.class);
                startActivity(vacationSignUp);
                finish();
            }
        }

        private void sendAddChildRequest(Map<String, String> addChildParamMap){

            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://lloyd.deanwyns.me/api")
                    .setConverter(new GsonConverter(gson))
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();
            RestService service = restAdapter.create(RestService.class);

            Callback<String> kind = new Callback<String>() {
                @Override
                public void success(String gebruiker, Response response) {
                    //Log.i(TAG, gebruiker.toString());
                    response.getBody();
                    Toast.makeText(getBaseContext(), "Toegevoegd", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    error.getCause();
                }

            };
            service.addChild(addChildParamMap);
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }
}
