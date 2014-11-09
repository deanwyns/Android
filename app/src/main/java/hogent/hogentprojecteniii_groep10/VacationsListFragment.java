package hogent.hogentprojecteniii_groep10;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import hogent.hogentprojecteniii_groep10.models.Vacation;

public class VacationsListFragment extends Fragment implements SearchView.OnQueryTextListener {
    private ArrayAdapter<Vacation> vacationAdapter;
    private View view;
    private ListView vacationListView;
    private List<Vacation> vacationList = new ArrayList<Vacation>();
    private OnListItemSelectedListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateVacationList();
        vacationAdapter = new VacationListAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_vacations_list, container, false);
        setupListviewAndAdapter();
        return view;
    }

    private void populateVacationList() {
        String promoTextBarkenTijn = "Recept voor een fantastische zomervakantie: toffe monitoren, leuke vrienden, een prachtig vakantiecentrum en véél fun en ambiance! De monitoren zorgen voor een afwisselend programma (strand- en duinspelen, daguitstappen, themaspelen, fuif, …) maar willen jou er natuurlijk bij. Wacht niet te lang en plan je vakantie naar zee met JOETZ!";
        Vacation barkentijnZomerLp = new Vacation(0, "JOETZ aan zee", "Uitgebreide beschrijving die momenteel korter is dan de promotext.", promoTextBarkenTijn, "De Barkentijn, Nieuwpoort", new GregorianCalendar(2014, 6, 3), new GregorianCalendar(2014, 6, 12), 4, 12, "busvervoer of eigen vervoer", 90, 400.00, 310.00, 220.00, true);
        String promoTextKrokus = "Verveling krijgt geen kans tijdens de krokusvakantie want op maandag 03 maart 2014 trekken we er met z’n allen op uit! We logeren in het vakantiecentrum “De Barkentijn” te Nieuwpoort.\n" +
                "Vijf dagen lang spelen we de leukste spelletjes, voor klein en groot. Samen met je vakantievriendjes beleef je het ene avontuur na het andere.  Plezier gegarandeerd!";
        Vacation krokusVakantie = new Vacation(1, "Krokusvakantie aan zee", "Een beschrijving die meer zegt dan de huidige promotext die blijkbaar niet beschikbaar is.", promoTextKrokus, "De Barkentijn, Nieuwpoort", new GregorianCalendar(2014, 8, 12), new GregorianCalendar(2014, 8, 24), 6, 16, "busvervoer of eigen vervoer", 20, 165.00, 135.00, 105.00, true);

        vacationList.add(barkentijnZomerLp);
        vacationList.add(krokusVakantie);
        vacationList.add(barkentijnZomerLp);
        vacationList.add(krokusVakantie);
        vacationList.add(barkentijnZomerLp);
        vacationList.add(krokusVakantie);
        vacationList.add(barkentijnZomerLp);
        vacationList.add(krokusVakantie);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnListItemSelectedListener) {
            listener = (OnListItemSelectedListener) activity;
        } else {
            throw new ClassCastException(
                    activity.toString()
                            + " must implement ItemsListFragment.OnListItemSelectedListener");
        }
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        vacationListView.setChoiceMode(activateOnItemClick ? ListView.CHOICE_MODE_SINGLE : ListView.CHOICE_MODE_NONE);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        filterOnTitle(query);
        return true;
    }

    private void filterOnTitle(String query) {
        List<Vacation> filteredVacationList = new ArrayList<Vacation>();

        for(Vacation v : vacationList){
            if(v.getTitle().toLowerCase().contains(query.toLowerCase()))
                filteredVacationList.add(v);
        }
        vacationAdapter.clear();
        vacationAdapter.addAll(filteredVacationList);
    }

    @Override
    public boolean onQueryTextChange(String query) {
        if(query.isEmpty())
        {
            Log.i("VacationOverview", query);
            vacationAdapter.clear();
            populateVacationList();
            setupListviewAndAdapter();
        }
        return false;
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
