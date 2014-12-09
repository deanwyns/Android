package hogent.hogentprojecteniii_groep10.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hogent.hogentprojecteniii_groep10.R;
import hogent.hogentprojecteniii_groep10.models.Gebruiker;
import hogent.hogentprojecteniii_groep10.models.Monitor;

/**
 * De activity die een bepaalde monitor zal vinden op basis van een gegeven naam.
 */
public class FindMonitorActivity extends Activity {

    private EditText findMonitorTxt;
    private Button findMonitorBtn;
    private ListView monitorListView;
    private TextView findMonitorHelpLbl;
    private ArrayAdapter<Monitor> adapter;
    private List<Monitor> monitorList = new ArrayList<Monitor>();

    //Zal opgevuld worden van de server als de back-end ooit wordt gemaakt.
    private List<Monitor> existingMonitorsList = new ArrayList<Monitor>(
            //Ik hoop dat de begeleider dit ook niet moet invullen? Zijn oudergegevens?
            Arrays.asList(new Monitor("email1", "pass", "0474685148", "naam1", "voornaam1"),
                    new Monitor("email2", "pass", "0474685148", "naam2", "voornaam2"),
                    new Monitor("email3", "pass", "0474685148", "naam3", "voornaam3"),
                    new Monitor("email4", "pass", "0474685148", "naam4", "voornaam4"))
    );

    /**
     * Vult de view op bij het aanmaken van de activity.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_monitor);

        findMonitorTxt = (EditText) findViewById(R.id.find_monitor_name_txt);
        findMonitorBtn = (Button) findViewById(R.id.find_monitor_btn);
        monitorListView = (ListView) findViewById(R.id.find_monitor_listview);
        findMonitorHelpLbl = (TextView) findViewById(R.id.find_monitor_help_lbl);

        setupListView();

        findMonitorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchedValue = findMonitorTxt.getText().toString();
                getMonitorsFromServer(searchedValue);
            }
        });
    }

    /**
     * Zal de lijst aanmaken op basis van de monitoren.
     * Wanneer er op een monitor geklikt wordt zal er een dialog getoond worden.
     */
    private void setupListView() {
        adapter = new ArrayAdapter<Monitor>(this, android.R.layout.simple_list_item_1, android.R.id.text1, monitorList);
        monitorListView.setAdapter(adapter);
        monitorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DialogFragment monitorDialog = new ShowMonitorDialogFragment();
                Bundle args = new Bundle();
                args.putParcelable("monitor", monitorList.get(position));
                monitorDialog.setArguments(args);
                monitorDialog.show(getFragmentManager(), "find_monitor_dialog");
            }
        });
    }

    /**
     * Zal de monitoren van de server ophalen en in de lijst plaatsen.
     * @param searchedValue de parameter waarop gezocht moet worden op de server
     */
    private void getMonitorsFromServer(String searchedValue) {
        monitorList.clear();

        for(Monitor g : existingMonitorsList){
            if(g.getNaam().toLowerCase().contains(searchedValue.toLowerCase()) || g.getVoornaam().toLowerCase().contains(searchedValue.toLowerCase()) ||
                    g.getEmailadres().toLowerCase().contains(searchedValue.toLowerCase()))
                monitorList.add(g);
        }

        adapter.notifyDataSetChanged();

        if(!monitorList.isEmpty())
            findMonitorHelpLbl.setVisibility(View.INVISIBLE);
        else{
            findMonitorHelpLbl.setText(getResources().getString(R.string.monitor_not_found));
            findMonitorHelpLbl.setVisibility(View.VISIBLE);
        }

    }

    /**
     * Zal het menu aanmaken op basis van de find_monitor xml.
     * Zal ook het icoon verwijderen.
     * @param menu het menu dat wordt opgevuld
     * @return bepaald hoe verdere menu processing wordt afgehandeld
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.find_monitor, menu);
        if (getActionBar() != null)
            getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        return true;
    }

    /**
     * Een klasse die een custom dialog zal tonen.
     */
    public static class ShowMonitorDialogFragment extends DialogFragment {
        private Monitor monitor;

        /**
         * De methode die de dialog zal maken en opvullen op basis van een monitor.
         * @param savedInstanceState
         * @return het aangemaakte dialoog
         */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            monitor = getArguments().getParcelable("monitor");
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            StringBuilder monitorData = new StringBuilder();
            monitorData.append(getResources().getString(R.string.naam)).append(": ").append(monitor.getVoornaam()).append(" ").append(monitor.getNaam());
            monitorData.append(System.lineSeparator()).append(getResources().getString(R.string.email)).append(": ").append(monitor.getEmailadres());
            monitorData.append(System.lineSeparator()).append(getResources().getString(R.string.telNr)).append(": ").append(monitor.getTelNr());
            builder.setMessage(monitorData.toString())
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            return builder.create();
        }
    }

}
