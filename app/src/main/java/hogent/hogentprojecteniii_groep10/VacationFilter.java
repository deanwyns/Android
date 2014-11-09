package hogent.hogentprojecteniii_groep10;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;


public class VacationFilter extends Activity {

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

                setResult(VacationOverview.FILTER_OPTION_REQUEST, returnedIntent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent returnedIntent = new Intent();
        setResult(VacationOverview.FILTER_OPTION_REQUEST, returnedIntent);
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
