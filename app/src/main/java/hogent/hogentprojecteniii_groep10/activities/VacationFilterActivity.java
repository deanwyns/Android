package hogent.hogentprojecteniii_groep10.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import hogent.hogentprojecteniii_groep10.R;


public class VacationFilterActivity extends Activity {

    private Button searchWithFilterBtn;
    private CheckBox ageFilterCheckbox;
    private TextView startAgeTxt, endAgeTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_filter);

        ageFilterCheckbox = (CheckBox) findViewById(R.id.age_filter_checkbox);
        searchWithFilterBtn = (Button) findViewById(R.id.search_with_filter_btn);
        startAgeTxt = (TextView) findViewById(R.id.start_age_input_txt);
        endAgeTxt = (TextView) findViewById(R.id.end_age_input_txt);

        addListeners();
    }

    private void addListeners() {
        searchWithFilterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnedIntent = new Intent();

                if(ageFilterCheckbox.isChecked())
                {
                    returnedIntent.putExtra("ageFilterChecked", true);

                    int startAge = 0;
                    if(!startAgeTxt.getText().toString().isEmpty())
                        startAge = Integer.parseInt(startAgeTxt.getText().toString());
                    int endAge = 99;
                    if(!endAgeTxt.getText().toString().isEmpty())
                        endAge = Integer.parseInt(endAgeTxt.getText().toString());

                    returnedIntent.putExtra("startAge", startAge);
                    returnedIntent.putExtra("endAge", endAge);
                }

                setResult(VacationsListActivity.FILTER_OPTION_REQUEST, returnedIntent);
                finish();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPref = getApplication().getSharedPreferences(getString(R.string.vacation_filter_preference_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.age_filter_checkbox_preference), ageFilterCheckbox.isChecked());
        editor.putInt(getString(R.string.age_filter_start_age_preference), Integer.parseInt(startAgeTxt.getText().toString()));
        editor.putInt(getString(R.string.age_filter_end_age_preference), Integer.parseInt(endAgeTxt.getText().toString()));
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPref = getApplication()
                .getSharedPreferences(getString(R.string.vacation_filter_preference_file), Context.MODE_PRIVATE);
        boolean checkboxChecked = sharedPref.getBoolean(getResources().getString(R.string.age_filter_checkbox_preference), false);
        ageFilterCheckbox.setChecked(checkboxChecked);
        int startAge = sharedPref.getInt(getResources().getString(R.string.age_filter_start_age_preference), 3);
        startAgeTxt.setText(Integer.toString(startAge));
        int endAge = sharedPref.getInt(getResources().getString(R.string.age_filter_end_age_preference), 30);
        endAgeTxt.setText(Integer.toString(endAge));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent returnedIntent = new Intent();
        setResult(VacationsListActivity.FILTER_OPTION_REQUEST, returnedIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.vacation_filter, menu);
        if (getActionBar() != null) {
            getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        }
        return true;
    }
}
