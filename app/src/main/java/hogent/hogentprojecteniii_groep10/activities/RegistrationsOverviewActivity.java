package hogent.hogentprojecteniii_groep10.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import hogent.hogentprojecteniii_groep10.R;
import hogent.hogentprojecteniii_groep10.helpers.RestClient;
import hogent.hogentprojecteniii_groep10.interfaces.RestService;
import hogent.hogentprojecteniii_groep10.models.ChildrenResponse;
import hogent.hogentprojecteniii_groep10.models.Kind;
import hogent.hogentprojecteniii_groep10.models.Registration;
import hogent.hogentprojecteniii_groep10.models.RegistrationsResponse;
import hogent.hogentprojecteniii_groep10.models.SingleVacationResponse;
import hogent.hogentprojecteniii_groep10.models.Vacation;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

/**
 * Toont een overzicht van inschrijvingen per kind
 */
public class RegistrationsOverviewActivity extends Activity {

    private ExpandableListAdapter listAdapter;
    private List<Kind> listDataHeader;
    private HashMap<Kind, List<Vacation>> listDataChild;
    private ExpandableListView childVacationListView;
    private RestClient restClient;

    /**
     * Maakt de activity aan
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrations_overview);

        childVacationListView = (ExpandableListView) findViewById(R.id.children_vacations_exp_listview);



        SharedPreferences sharedPref =
                getApplication().getSharedPreferences(getString(R.string.authorization_preference_file), Context.MODE_PRIVATE);
        String token = sharedPref.getString(getResources().getString(R.string.authorization), "No token");

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        restClient = new RestClient(token, gson);

        prepareListData();
        //prepareDummyListData();


        //Doet momenteel nog niks. Indien tijd over het naar de vakantie laten gaan.
        childVacationListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return false;
            }
        });

    }

    private void prepareAdapter() {
        listAdapter = new ExpandableListAdapter(getApplicationContext(), listDataHeader, listDataChild);
        childVacationListView.setAdapter(listAdapter);
    }

    /**
     * Bereid alle data voor.
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<Kind>();
        listDataChild = new HashMap<Kind, List<Vacation>>();

        new GetChildrenAndVacationsTask().execute();
    }

    /**
     * Bereid alle data voor.
     * Zou alles van de server moeten halen, maar is nog niet beschikbaar in de back-end.
     * Nu is er wat dummy-data.
     */
    private void prepareDummyListData() {
        listDataHeader = new ArrayList<Kind>();
        listDataChild = new HashMap<Kind, List<Vacation>>();

        Kind dummyChild = new Kind("De Mei", "Jan", "123546879", "Stad");

        // Adding child data
        listDataHeader.add(dummyChild);


        List<Vacation> vacationsForChild = new ArrayList<Vacation>();
        String promoTextBarkenTijn = "Recept voor een fantastische zomervakantie: toffe monitoren, leuke vrienden, een prachtig vakantiecentrum en véél fun en ambiance! De monitoren zorgen voor een afwisselend programma (strand- en duinspelen, daguitstappen, themaspelen, fuif, …) maar willen jou er natuurlijk bij. Wacht niet te lang en plan je vakantie naar zee met JOETZ!";
        Vacation barkentijnZomerLp = new Vacation(0, "JOETZ aan zee", "Uitgebreide beschrijving die momenteel korter is dan de promotext.", promoTextBarkenTijn, "De Barkentijn, Nieuwpoort", new Date(), new Date(), 5, 16, "busvervoer of eigen vervoer", 90, 400.00, 310.00, 220.00, 1);
        String promoTextKrokus = "Verveling krijgt geen kans tijdens de krokusvakantie want op maandag 03 maart 2014 trekken we er met z’n allen op uit! We logeren in het vakantiecentrum “De Barkentijn” te Nieuwpoort.\n" +
                "Vijf dagen lang spelen we de leukste spelletjes, voor klein en groot. Samen met je vakantievriendjes beleef je het ene avontuur na het andere. Plezier gegarandeerd!";
        Vacation krokusVakantie = new Vacation(1, "Krokusvakantie aan zee", "Een beschrijving die meer zegt dan de huidige promotext die blijkbaar niet beschikbaar is.", promoTextKrokus, "De Barkentijn, Nieuwpoort", new Date(), new Date(), 5, 16, "busvervoer of eigen vervoer", 20, 165.00, 135.00, 105.00, 1);

        vacationsForChild.add(barkentijnZomerLp);
        vacationsForChild.add(krokusVakantie);

        listDataChild.put(dummyChild, vacationsForChild);

        prepareAdapter();
    }


    /**
     * Maakt het menu aan en maakt het icoon onzichtbaar
     *
     * @param menu het menu dat wordt bewerkt
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.registrations_overview, menu);
        if (getActionBar() != null)
            getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        return true;
    }

    public class GetChildrenAndVacationsTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(RegistrationsOverviewActivity.this, getResources().getString(R.string.getting_signups), getResources().getString(R.string.please_wait), true);
            super.onPreExecute();
        }



        /**
         * Haalt kinderen, hun inschrijven en de bijhorende vakanties op uit de back-end.
         *
         * @param voids
         * @return
         */
        @Override
        protected Void doInBackground(Void... voids) {
            List<Kind> children = new ArrayList<Kind>();
            try{
                ChildrenResponse childrenResponse = restClient.getRestService().getChildren();
                children = childrenResponse.getChildren();
            }catch (RetrofitError error){
                error.printStackTrace();
            }

            listDataHeader = new ArrayList<Kind>();
            listDataChild = new HashMap<Kind, List<Vacation>>();

            for (Kind k : children) {
                listDataHeader.add(k);
                List<Vacation> vacationsForChild = new ArrayList<Vacation>();
                try {
                    RegistrationsResponse registrationsResponse = restClient.getRestService().getRegistrationsForChild(k.getId());
                    List<Registration> registrations = registrationsResponse.getRegistrations();

                    for (Registration v : registrations) {
                        SingleVacationResponse currentVacationResponse = restClient.getRestService().getVacation(v.getVacationId());
                        Vacation currentVacation = currentVacationResponse.getVacation();
                        vacationsForChild.add(currentVacation);
                    }
                    listDataChild.put(k, vacationsForChild);
                } catch (RetrofitError e) {
                    listDataHeader.remove(k);
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    prepareAdapter();
                    if (listDataChild.isEmpty())
                        Toast.makeText(getApplicationContext(), "Er zijn geen inschrijvingen!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    /**
     * Een ExpandableListAdapter om de vakanties per kind te tonen.
     */
    public class ExpandableListAdapter extends BaseExpandableListAdapter {

        private Context _context;
        private List<Kind> _listDataHeader;
        private HashMap<Kind, List<Vacation>> _listDataChild;

        /**
         * Constructor van de adapter
         *
         * @param context        de context van de applicatie
         * @param listDataHeader de kinderen die een categorie voorstellen
         * @param listChildData  de vakanties die bij een categorie (kind) behoren
         */
        public ExpandableListAdapter(Context context, List<Kind> listDataHeader,
                                     HashMap<Kind, List<Vacation>> listChildData) {
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
        }

        /**
         * Haalt het vakantie object op
         *
         * @param groupPosition  de positie van de groep
         * @param childPosititon de positie van het kind
         * @return het geklikte object
         */
        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        /**
         * maakt de view van een child (vacation) aan.
         *
         * @param groupPosition
         * @param childPosition
         * @param isLastChild
         * @param convertView
         * @param parent
         * @return
         */
        @Override
        public View getChildView(int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            final String childText = getChild(groupPosition, childPosition).toString();

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.child_vacation_list_item, null);
            }

            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.lblListItem);

            txtListChild.setText(childText);
            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listDataHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this._listDataHeader.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        /**
         * Maakt de categorie view (kind) aan.
         *
         * @param groupPosition
         * @param isExpanded
         * @param convertView
         * @param parent
         * @return
         */
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            String headerTitle = getGroup(groupPosition).toString();
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.child_vacation_list_group, null);
            }

            TextView lblListHeader = (TextView) convertView
                    .findViewById(R.id.lblListHeader);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

}
