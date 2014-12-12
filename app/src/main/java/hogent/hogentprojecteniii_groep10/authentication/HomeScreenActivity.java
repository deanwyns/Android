package hogent.hogentprojecteniii_groep10.authentication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import hogent.hogentprojecteniii_groep10.R;


public class HomeScreenActivity extends Activity implements OnClickListener {

    private Button btnSignIn, btnSignUp, btnSignOut;
    private LinearLayout signInForm, signOutForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        btnSignIn = (Button) findViewById(R.id.btnSingIn);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        btnSignOut = (Button) findViewById(R.id.btnSingOut);
        signInForm = (LinearLayout)findViewById(R.id.login_form_buttons);
        signOutForm = (LinearLayout)findViewById(R.id.logout_form_buttons);

        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);

        showButtonsForLoggedIn(readLoginData());
    }
    @Override
    public void onClick(View v) {
        Intent i = null;
        switch(v.getId()){
            case R.id.btnSingIn:
                i = new Intent(this, Login.class);
                break;
            case R.id.btnSignUp:
                i = new Intent(this, Registreren.class);
                break;
            case R.id.btnSingOut:
                signOut();
                break;
        }
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showButtonsForLoggedIn(readLoginData());
    }

    public void signOut(){
        SharedPreferences sharedPref = getApplication()
                .getSharedPreferences(
                        getString(R.string.authorization_preference_file),
                        Context.MODE_PRIVATE);
        Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }

    private boolean readLoginData() {
        boolean isLoggedIn;
        SharedPreferences sharedPref = getApplication()
                .getSharedPreferences(
                        getString(R.string.authorization_preference_file),
                        Context.MODE_PRIVATE);
        String token = sharedPref.getString(getResources().getString(R.string.authorization), "No token");
        isLoggedIn = !token.equals("No token");
        return isLoggedIn;
    }

    private void showButtonsForLoggedIn(boolean isLoggedIn) {
        if(isLoggedIn){
            signOutForm.setVisibility(View.VISIBLE);
            signInForm.setVisibility(View.INVISIBLE);
        }else{
            signOutForm.setVisibility(View.INVISIBLE);
            signInForm.setVisibility(View.VISIBLE);
        }
    }

}
