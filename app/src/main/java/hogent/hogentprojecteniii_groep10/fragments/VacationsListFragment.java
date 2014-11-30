package hogent.hogentprojecteniii_groep10.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import hogent.hogentprojecteniii_groep10.R;
import hogent.hogentprojecteniii_groep10.activities.VacationsListActivity;
import hogent.hogentprojecteniii_groep10.helpers.NetworkingMethods;
import hogent.hogentprojecteniii_groep10.interfaces.RestService;
import hogent.hogentprojecteniii_groep10.models.Vacation;
import hogent.hogentprojecteniii_groep10.models.VacationResponse;
import hogent.hogentprojecteniii_groep10.persistence.SQLiteHelper;
import hogent.hogentprojecteniii_groep10.persistence.VacationDataSource;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vacationDataSource = new VacationDataSource(getActivity().getApplicationContext());
        prepareRestAdapter();
    }

    private void prepareRestAdapter() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setConverter(new GsonConverter(gson))
                .build();
        service = restAdapter.create(RestService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_vacations_list, container, false);
        populateVacationList();
        sortByTitleBtn = (Button) view.findViewById(R.id.sort_title_btn);
        sortByDateBtn = (Button) view.findViewById(R.id.sort_date_btn);
        setActionListeners();
        return view;
    }

    private void populateVacationList() {
        new DownloadTask(getActivity()).execute();
    }

    private void storeVacationsInSQLiteDB(List<Vacation> vacationList) {
        vacationDataSource.close();
        getActivity().deleteDatabase("vacations.db");
        vacationDataSource.open();
        for (Vacation v : vacationList) {
            vacationDataSource.createVacation(v);
        }
    }

    private List<Vacation> getVacationsFromSQLiteDB() {
        vacationDataSource.open();
        List<Vacation> vacations = vacationDataSource.getAllVacations();
        return vacations;
    }

    @Override
    public void onResume() {
        vacationDataSource.open();
        super.onResume();
    }

    @Override
    public void onPause() {
        vacationDataSource.close();
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        vacationAdapter.clear();
        populateVacationList();
        if (requestCode == FILTER_OPTION_REQUEST && data != null) {
            if (data.getBooleanExtra("ageFilterChecked", false)) {
                int startAge = data.getIntExtra("startAge", 0);
                int endAge = data.getIntExtra("endAge", 99);
                filterOnAges(startAge, endAge);
            }
        }
    }

    private void filterOnAges(int startAge, int endAge) {
        List<Vacation> filteredVacationList = new ArrayList<Vacation>();

        for (Vacation v : vacationList) {
            if (v.getAgeFrom() <= startAge && v.getAgeTo() >= endAge)
                filteredVacationList.add(v);
        }

        vacationAdapter.clear();
        vacationAdapter.addAll(filteredVacationList);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnListItemSelectedListener) {
            listener = (OnListItemSelectedListener) activity;
        } else {
            throw new ClassCastException(activity.toString() + " must implement VacationsListFragment.OnListItemSelectedListener");
        }
    }

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

    public boolean onQueryTextSubmit(String query) {
        filterOnTitle(query);
        return true;
    }

    private void filterOnTitle(String query) {
        List<Vacation> filteredVacationList = new ArrayList<Vacation>();

        for (Vacation v : vacationList) {
            if (v.getTitle().toLowerCase().contains(query.toLowerCase()))
                filteredVacationList.add(v);
        }
        vacationAdapter.clear();
        vacationAdapter.addAll(filteredVacationList);
    }

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

    private class VacationListAdapter extends ArrayAdapter<Vacation> implements Filterable {

        public VacationListAdapter(List<Vacation> vacations) {
            super(getActivity().getApplicationContext(), R.layout.vacation_item_view, vacations);
        }

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

    public class DownloadTask extends AsyncTask<String, Integer, Boolean>{
        private ProgressDialog progressDialog;
        private Context currentContext = null;

        public DownloadTask(Context context){
            currentContext = context;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(), getResources().getString(R.string.getting_vacations), getResources().getString(R.string.please_wait), true);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean o) {
            setupListviewAndAdapter();
            progressDialog.dismiss();
            super.onPostExecute(o);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            //Is er internet?
            if (NetworkingMethods.isNetworkAvailable(getActivity().getApplicationContext())) {
                //Er is internet. Haal het van de server afhankelijk of deze DB en de server DB gelijk zijn

                //Is de versie van de db anders dan die op de server?
                //TODO: Implementeer code voor het checken van versies.
                if (true) {
                    //De versie is niet gelijk, haal het van de server
                    VacationResponse response = null;
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
