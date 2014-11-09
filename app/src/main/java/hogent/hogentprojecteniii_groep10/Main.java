package hogent.hogentprojecteniii_groep10;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;


public class Main extends Activity {

    int onStartCount = 0;
    private LinearLayout ll;
    private ImageButton getVacationsBtn, getPhotosBtn;
    private final static String TAG = "MAIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        onStartCount = 1;
        if (savedInstanceState == null) // 1st time
        {
            this.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        } else // already created so reverse animation
        {
            onStartCount = 2;
        }

//        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
//        setSupportActionBar(toolbar);
        ll = (LinearLayout) findViewById(R.id.newsfeed_id);
        getVacationsBtn = (ImageButton) findViewById(R.id.camp_btn);
        getPhotosBtn = (ImageButton) findViewById(R.id.photo_btn);

        setupListeners();

    }



    private void readLoginData() {
        SharedPreferences sharedPref = getApplication()
                .getSharedPreferences(getString(R.string.authorization_preference_file), Context.MODE_PRIVATE);
        String token = sharedPref.getString(getResources().getString(R.string.authorization), "No token");
        Toast toast = Toast.makeText(getApplicationContext(), token, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (onStartCount > 1) {
            this.overridePendingTransition(R.anim.slide_in_right,
                    R.anim.slide_out_right);

        } else if (onStartCount == 1) {
            onStartCount++;
        }

    }

    private void setupListeners() {
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "Newsfeed clicked");
            }
        });

        getVacationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent vacationOverviewIntent = new Intent(getApplicationContext(), VacationOverview.class);
                startActivity(vacationOverviewIntent);
            }
        });

        getPhotosBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Om de access token te testen. Wordt natuurlijk vervangen door foto's
                readLoginData();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_login) {
            Intent login = new Intent(getApplicationContext(), Login.class);
            startActivity(login);
        }
        return super.onOptionsItemSelected(item);
    }
}
