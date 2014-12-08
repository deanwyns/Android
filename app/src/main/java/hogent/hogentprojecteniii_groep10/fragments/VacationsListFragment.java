package hogent.hogentprojecteniii_groep10.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import hogent.hogentprojecteniii_groep10.R;
import hogent.hogentprojecteniii_groep10.helpers.NetworkingMethods;
import hogent.hogentprojecteniii_groep10.interfaces.RestService;
import hogent.hogentprojecteniii_groep10.models.Vacation;
import hogent.hogentprojecteniii_groep10.models.VacationResponse;
import hogent.hogentprojecteniii_groep10.persistence.VacationDataSource;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Het fragment dat een lijst van vakanties voorstelt.
 * Implementeert een onquerytextlistener om te kunnen zoeken op de huidige vakanties.
 */
public class VacationsListFragment extends Fragment implements SearchView.OnQueryTextListener {
    private ArrayAdapter<Vacation> vacationAdapter;
    private View view;
    private ListView vacationListView;
    private List<Vacation> vacationList = new ArrayList<Vacation>();
    private OnListItemSelectedListener listener;
    private boolean titleSortedAscending, dateSortedAscending;
    private Button sortByTitleBtn, sortByDateBtn;
    private final String ENDPOINT = "http://lloyd.deanwyns.me/api";
    private RestService service;
    public static final int FILTER_OPTION_REQUEST = 1;
    private VacationDataSource vacationDataSource;

    /**
     * Zal het de restadapter en de vacationDataSource initialiseren.
     * De view zelf wordt opgebouwd in de onCreateView.
     * Dit omdat er gebruik gemaakt wordt van een master/detail overzicht.
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vacationDataSource = new VacationDataSource(getActivity().getApplicationContext());
        prepareRestAdapter();
    }

    /**
     * De restadapter wordt gemaakt die zal gebruikt worden om vakanties op te halen.
     */
    private void prepareRestAdapter() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setConverter(new GsonConverter(gson))
                .build();
        service = restAdapter.create(RestService.class);
    }

    /**
     * De view wordt gemaakt op basis van het fragment_vacations_list xml bestand.
     * @param inflater instantieert de xml voor een view object
     * @param container
     * @param savedInstanceState
     * @return de view die gemaakt werd
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_vacations_list, container, false);
        populateVacationList();
        sortByTitleBtn = (Button) view.findViewById(R.id.sort_title_btn);
        sortByDateBtn = (Button) view.findViewById(R.id.sort_date_btn);
        setActionListeners();
        return view;
    }

    /**
     * Zal de lijst met vakanties opvullen via een AsyncTask
     */
    private void populateVacationList() {
        new VacationDownloadTask(getActivity()).execute();
    }

    /**
     * Zal eerst de databank verwijderen en daarna een nieuwe databank maken en de vakanties daarin plaatsen.
     * @param vacationList de vakanties die worden opgeslagen
     */
    private void storeVacationsInSQLiteDB(List<Vacation> vacationList) {
        vacationDataSource.close();
        getActivity().deleteDatabase("vacations.db");
        vacationDataSource.open();
        for (Vacation v : vacationList) {
            vacationDataSource.createVacation(v);
        }
    }

    /**
     * Zal alle vakanties uit de databank halen
     * @return de vakanties die opgehaald zijn uit de databank
     */
    private List<Vacation> getVacationsFromSQLiteDB() {
        vacationDataSource.open();
        return vacationDataSource.getAllVacations();
    }

    /**
     * zal de databank openen wanneer de applicatie verder gaat.
     */
    @Override
    public void onResume() {
        vacationDataSource.open();
        super.onResume();
    }

    /**
     * Zal de databank sluiten als de applicatie pauseert.
     */
    @Override
    public void onPause() {
        vacationDataSource.close();
        super.onPause();
    }

    /**
     * Deze methode zal opgeroepen worden nadat StartActivityForResult is opgeroepen met betrekking tot een filter.
     * @param requestCode de code die gebruikt werd om een activity te identificeren
     * @param resultCode de code die aangeeft of de activity juist is afgehandeld
     * @param data de data die is meegegeven door de activity
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        vacationAdapter.clear();
        try {
            new VacationDownloadTask(getActivity()).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (requestCode == FILTER_OPTION_REQUEST && data != null) {
            if (data.getBooleanExtra("ageFilterChecked", false)) {
                int startAge = data.getIntExtra("startAge", 0);
                int endAge = data.getIntExtra("endAge", 99);
                filterOnAges(startAge, endAge);
            }
        }
    }

    /**
     * Zal filteren op leeftijden.
     * @param startAge de minimum leeftijd voor een kind voor een bepaalde vakantie
     * @param endAge de maximum leeftijd voor een kind voor een bepaalde vakantie
     */
    private void filterOnAges(int startAge, int endAge) {
        List<Vacation> filteredVacationList = new ArrayList<Vacation>();
        for (Vacation v : vacationList) {
            if (v.getAgeFrom() <= startAge && v.getAgeTo() >= endAge)
                filteredVacationList.add(v);
        }
        vacationList = filteredVacationList;
        vacationAdapter.clear();
        vacationAdapter.addAll(filteredVacationList);
        vacationAdapter.notifyDataSetChanged();
    }

    /**
     * Onderdeel van de search functie.
     * De activity (VacationsListActivity) moet onListItemSelectedListener implementeren.
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnListItemSelectedListener) {
            listener = (OnListItemSelectedListener) activity;
        } else {
            throw new ClassCastException(activity.toString() + " must implement VacationsListFragment.OnListItemSelectedListener");
        }
    }

    /**
     * Bereid de action listeners van de activity voor.
     */
    private void setActionListeners() {
        sortByTitleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleBtnText = sortByTitleBtn.getText().toString();
                StringBuilder adjustedTitleBtnText = new StringBuilder(titleBtnText);
                if (titleSortedAscending)
                    adjustedTitleBtnText.replace(titleBtnText.length() - 1, titleBtnText.length(), "v");
                else
                    adjustedTitleBtnText.replace(titleBtnText.length() - 1, titleBtnText.length(), "^");
                sortByTitleBtn.setText(adjustedTitleBtnText.toString());

                Collections.sort(vacationList, new Comparator<Vacation>() {
                    @Override
                    public int compare(Vacation lhs, Vacation rhs) {
                        if (!titleSortedAscending) {
                            return lhs.getTitle().toLowerCase().compareTo(rhs.getTitle().toLowerCase());
                        } else {
                            return rhs.getTitle().toLowerCase().compareTo(lhs.getTitle().toLowerCase());
                        }
                    }
                });
                titleSortedAscending = !titleSortedAscending;
                vacationAdapter.notifyDataSetChanged();
            }

        });
        sortByDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateBtnText = sortByDateBtn.getText().toString();
                StringBuilder adjustedDateBtnText = new StringBuilder(dateBtnText);
                if (dateSortedAscending)
                    adjustedDateBtnText.replace(dateBtnText.length() - 1, dateBtnText.length(), "v");
                else
                    adjustedDateBtnText.replace(dateBtnText.length() - 1, dateBtnText.length(), "^");
                sortByDateBtn.setText(adjustedDateBtnText.toString());

                Collections.sort(vacationList, new Comparator<Vacation>() {
                    @Override
                    public int compare(Vacation lhs, Vacation rhs) {
                        if (!dateSortedAscending)
                            return lhs.getBeginDate().compareTo(rhs.getBeginDate());
                        else
                            return rhs.getBeginDate().compareTo(lhs.getBeginDate());
                    }
                });
                dateSortedAscending = !dateSortedAscending;
                vacationAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Bereid de listview en de custom adapter voor.
     */
    private void setupListviewAndAdapter() {
        vacationAdapter = new VacationListAdapter(vacationList);
        vacationListView = (ListView) view.findViewById(R.id.vacation_overview_list_view);
        vacationListView.setAdapter(vacationAdapter);
        vacationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View item, int position, long rowId) {
                // Retrieve item based on position
                Vacation vacation = vacationAdapter.getItem(position);
                // Fire selected listener event with item
                listener.onItemSelected(vacation);
            }
        });
    }

    /**
     * Vangt queries op van de searchfunctie
     * @param query de zoekquery
     * @return true als de query is afgehandeld door de listener
     */
    public boolean onQueryTextSubmit(String query) {
        filterOnTitle(query);
        return true;
    }

    /**
     * Zal filteren op titel volgens de gegeven query van onQueryTextSubmit.
     * @param query de query waarop gezocht wordt
     */
    private void filterOnTitle(String query) {
        List<Vacation> filteredVacationList = new ArrayList<Vacation>();

        for (Vacation v : vacationList) {
            if (v.getTitle().toLowerCase().contains(query.toLowerCase()))
                filteredVacationList.add(v);
        }
        vacationAdapter.clear();
        vacationAdapter.addAll(filteredVacationList);
    }

    /**
     * Indien de query verandert en de tekst is leeg, zal de volledige lijst getoond worden.
     * @param query de query waarop gezocht wordt
     * @return
     */
    public boolean onQueryTextChange(String query) {
        if (query.isEmpty()) {
            vacationAdapter.clear();
            populateVacationList();
        }
        return false;
    }

    public interface OnListItemSelectedListener {
        public void onItemSelected(Vacation vacation);
    }

    /**
     * De custom adapter waarmee vakanties in een lijst zullen getoond worden.
     */
    private class VacationListAdapter extends ArrayAdapter<Vacation> {

        /**
         * De constructor die de adapter zal aanmaken.
         * @param vacations de vakanties die de lijst zullen opvullen
         */
        public VacationListAdapter(List<Vacation> vacations) {
            super(getActivity().getApplicationContext(), R.layout.vacation_item_view, vacations);
        }

        /**
         * Zal de view opmaken van elke vakantie.
         * @param position de positie van de huidige view
         * @param convertView de oude view om te hergebruiken
         * @param parent de ouder waarop deze view geplaatst wordt
         * @return de view met de vakantie
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Haal de view op die ingevuld moet worden
            View itemView = convertView;
            if (itemView == null) {
                itemView = getActivity().getLayoutInflater().inflate(R.layout.vacation_item_view, parent, false);
            }

            //Geselecteerde vakantie zoeken
            Vacation currentVacation = vacationList.get(position);

            //Afbeelding opzetten
            ImageView imageView = (ImageView) itemView.findViewById(R.id.vacation_icon);
            //Het zou makkelijker zijn om een id in de model te steken en op basis daarvan de afbeelding te selecteren.
            if (currentVacation.getTitle().startsWith("Fiets"))
                imageView.setImageResource(R.drawable.biking);
            else if (currentVacation.getTitle().startsWith("Zwem"))
                imageView.setImageResource(R.drawable.swimming);
            else
                imageView.setImageResource(R.drawable.sports);

            //Verder de view opvullen
            TextView titleTxt = (TextView) itemView.findViewById(R.id.vacation_title_lbl);
            titleTxt.setText(currentVacation.getTitle());
            TextView descTxt = (TextView) itemView.findViewById(R.id.vacation_desc_lbl);
            descTxt.setText(currentVacation.getPromoText());
            TextView beginDateTxt = (TextView) itemView.findViewById(R.id.vacation_begindate_lbl);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            beginDateTxt.setText(formatter.format(currentVacation.getBeginDate().getTime()));

            return itemView;
        }
    }

    /**
     * Een AsyncTask die de vakanties zal downloaden van de server.
     */
    public class VacationDownloadTask extends AsyncTask<String, Integer, Boolean>{
        private ProgressDialog progressDialog;
        private Context currentContext = null;

        /**
         * De constructor die de context meekrijgt
         * @param context de applicatiecontext
         */
        public VacationDownloadTask(Context context){
            currentContext = context;
        }

        /**
         * Voordat de task gestart wordt zal er een dialog getoond worden
         */
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(), getResources().getString(R.string.getting_vacations), getResources().getString(R.string.please_wait), true);
            super.onPreExecute();
        }

        /**
         * Na het downloaden zal de adapter ingesteld worden en de progressdialog verwijderd worden.
         * @param o geef terug of de download geslaagd is
         */
        @Override
        protected void onPostExecute(Boolean o) {
            setupListviewAndAdapter();
            progressDialog.dismiss();
            super.onPostExecute(o);
        }

        /**
         * Download de vakanties van de server als er internet is.
         * Haalt ze uit de databank indien er geen internet is.
         * @param params eventuele parameters die konden meegegeven worden
         * @return true als de download geslaagd is
         */
        @Override
        protected Boolean doInBackground(String... params) {
            //Is er internet?
            if (NetworkingMethods.isNetworkAvailable(getActivity().getApplicationContext())) {
                //Er is internet. Haal het van de server afhankelijk of deze DB en de server DB gelijk zijn

                //Is de versie van de db anders dan die op de server?
                //TODO: Implementeer code voor het checken van versies.
                if (true) {
                    //De versie is niet gelijk, haal het van de server
                    VacationResponse response;
                    response = service.getVacationOverview();
                    if (response != null && response.getVacations() != null)
                        vacationList = response.getVacations();
                    //Vervang de DB door de nieuwe vakanties
                    storeVacationsInSQLiteDB(vacationList);

                } else {
                    //De versie is gelijk, haal het uit de android DB
                    vacationList = getVacationsFromSQLiteDB();
                }
            } else {
                //Er is geen internet, haal de vakanties uit de android DB
                vacationList = getVacationsFromSQLiteDB();
            }
            return true;
        }
    }
}
