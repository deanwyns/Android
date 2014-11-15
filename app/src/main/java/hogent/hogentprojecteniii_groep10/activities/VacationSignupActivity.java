package hogent.hogentprojecteniii_groep10.activities;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import hogent.hogentprojecteniii_groep10.R;
import hogent.hogentprojecteniii_groep10.models.Gebruiker;
import hogent.hogentprojecteniii_groep10.models.Vacation;


public class VacationSignupActivity extends Activity {

    private Button addChildToAccountBtn, goToBillingBtn, addChildSpinnerBtn;
    private LinearLayout childrenSpinnerLayout;
    private Spinner firstChildSpinner;

    private Gebruiker[] tempArray;
    private ArrayAdapter<Gebruiker> adapter;
    private Vacation vacation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_signup);

        vacation = (Vacation) getIntent().getParcelableExtra("SpecificVacation");

        addChildToAccountBtn = (Button) findViewById(R.id.add_child_to_account_btn);
        goToBillingBtn = (Button) findViewById(R.id.go_to_billing_btn);
        addChildSpinnerBtn = (Button) findViewById(R.id.add_child_spinner_btn);
        childrenSpinnerLayout = (LinearLayout) findViewById(R.id.linear_layout_children_spinners);
        firstChildSpinner  = (Spinner) findViewById(R.id.first_child_spinner);

        Gebruiker child1 = new Gebruiker("email", "pass", "000", "Kind1", "Kind1", "000", "ouder1", "ouder2", "000");
        Gebruiker child2 = new Gebruiker("email2", "pass2", "000", "Kind2", "Kind2", "000", "ouder1", "ouder2", "000");
        Gebruiker child3 = new Gebruiker("email3", "pass3", "000", "Kind3", "Kind3", "000", "ouder1", "ouder2", "000");
        tempArray = new Gebruiker[]{child1, child2, child3};

        adapter = new ArrayAdapter<Gebruiker>(getApplicationContext(), android.R.layout.simple_spinner_item, tempArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        firstChildSpinner.setAdapter(adapter);

        setUpListeners();
    }

    private void setUpListeners() {
        addChildSpinnerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Spinner childSpinner = new Spinner(getApplicationContext());
                Spinner childSpinner = new Spinner(getApplicationContext(), null, android.R.attr.spinnerStyle);
                childSpinner.setAdapter(adapter);
                childrenSpinnerLayout.addView(childSpinner);
            }
        });

        addChildToAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast todo = Toast.makeText(getApplicationContext(), "Wordt gemaakt door Fabrice", Toast.LENGTH_SHORT);
                todo.show();
            }
        });

        goToBillingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.vacation_signup, menu);
        if( getActionBar() != null)
        {
            getActionBar().setTitle(getResources().getString(R.string.title_activity_vacation_signup) + " " + vacation.getTitle());
            getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        }

        return true;
    }

}
