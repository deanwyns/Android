package hogent.hogentprojecteniii_groep10.fragments;

import android.app.Activity;
import android.content.Intent;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import hogent.hogentprojecteniii_groep10.R;
import hogent.hogentprojecteniii_groep10.interfaces.RestService;
import hogent.hogentprojecteniii_groep10.models.Vacation;
import hogent.hogentprojecteniii_groep10.models.VacationResponse;
import retrofit.RestAdapter;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(ENDPOINT)
                .setConverter(new GsonConverter(gson))
                .build();
        service = restAdapter.create(RestService.class);
        populateVacationList();
        vacationAdapter = new VacationListAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_vacations_list, container, false);
        setupListviewAndAdapter();
        sortByTitleBtn = (Button) view.findViewById(R.id.sort_title_btn);
        sortByDateBtn = (Button) view.findViewById(R.id.sort_date_btn);
        setActionListeners();
        return view;
    }

    private void populateVacationList() {

        VacationResponse response = null;
        try {
            response = new AsyncTask<Void, Void, VacationResponse>() {
                @Override
                protected VacationResponse doInBackground(Void... voids) {
                    return service.getVacationOverview();
                }
            }.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        vacationList = response.getVacations();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        vacationAdapter.clear();
        populateVacationList();
        setupListviewAndAdapter();
        if (requestCode == FILTER_OPTION_REQUEST && data != null) {
            if(data.getBooleanExtra("ageFilterChecked", false)){
                int startAge = data.getIntExtra("startAge", 0);
                int endAge = data.getIntExtra("endAge", 99);
                filterOnAges(startAge, endAge);
            }
        }
    }

    private void filterOnAges(int startAge, int endAge) {
        List<Vacation> filteredVacationList = new ArrayList<Vacation>();

        for(Vacation v : vacationList){
            if(v.getAgeFrom() <= startAge && v.getAgeTo() >= endAge)
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
                Collections.sort(vacationList, new Comparator<Vacation>() {
                    @Override
                    public int compare(Vacation lhs, Vacation rhs) {
                        if (!titleSortedAscending)
                            return lhs.getTitle().compareTo(rhs.getTitle());
                        else
                            return rhs.getTitle().compareTo(lhs.getTitle());
                    }
                });
                titleSortedAscending = !titleSortedAscending;
                vacationAdapter.notifyDataSetChanged();
            }
        });
        sortByDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        vacationAdapter = new VacationListAdapter();
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
            setupListviewAndAdapter();
        }
        return false;
    }


    public interface OnListItemSelectedListener {
        public void onItemSelected(Vacation vacation);
    }

    private class VacationListAdapter extends ArrayAdapter<Vacation> implements Filterable {

        public VacationListAdapter() {
            super(getActivity().getApplicationContext(), R.layout.vacation_item_view, vacationList);
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
}
