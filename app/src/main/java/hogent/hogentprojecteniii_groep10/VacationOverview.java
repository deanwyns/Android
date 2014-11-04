package hogent.hogentprojecteniii_groep10;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import hogent.hogentprojecteniii_groep10.models.Vacation;


public class VacationOverview extends Main implements SearchView.OnQueryTextListener {

    private boolean titleSortedAscending, dateSortedAscending;
    private Button sortByTitleBtn, sortByDateBtn;
    private SearchView mSearchView;
    private ArrayAdapter<Vacation> vacationAdapter;
    private List<Vacation> vacationList = new ArrayList<Vacation>();
    public static final int FILTER_OPTION_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_overview);

        sortByTitleBtn =(Button) findViewById(R.id.sort_title_btn);
        sortByDateBtn =(Button) findViewById(R.id.sort_date_btn);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
//        setSupportActionBar(toolbar);


        populateVacationList();
        populateListView();
        setActionListeners();
        registerClickCallback();

    }

    private void setActionListeners() {
        sortByTitleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(vacationList, new Comparator<Vacation>() {
                    @Override
                    public int compare(Vacation lhs, Vacation rhs) {
                        if(!titleSortedAscending)
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
                        if(!dateSortedAscending)
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

    private void populateVacationList() {
        String promoTextBarkenTijn = "Recept voor een fantastische zomervakantie: toffe monitoren, leuke vrienden, een prachtig vakantiecentrum en véél fun en ambiance! De monitoren zorgen voor een afwisselend programma (strand- en duinspelen, daguitstappen, themaspelen, fuif, …) maar willen jou er natuurlijk bij. Wacht niet te lang en plan je vakantie naar zee met JOETZ!";
        Vacation barkentijnZomerLp = new Vacation(0, "JOETZ aan zee", "Uitgebreide beschrijving die momenteel korter is dan de promotext.", promoTextBarkenTijn, "De Barkentijn, Nieuwpoort", new GregorianCalendar(2014, 6, 3), new GregorianCalendar(2014, 6, 12),4, 12, "busvervoer of eigen vervoer", 90, 400.00, 310.00, 220.00, true);
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

    private void populateListView() {
        vacationAdapter = new VacationListAdapter();
        ListView vacationList = (ListView) findViewById(R.id.vacation_overview_list_view);
        vacationList.setAdapter(vacationAdapter);
    }

    private void registerClickCallback() {
        ListView vacList = (ListView) findViewById(R.id.vacation_overview_list_view);
        vacList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View viewClicked, int pos, long id) {
                Vacation clickedVacation = vacationList.get(pos);
                Intent specificVacation = new Intent(getApplicationContext(), SpecificVacationView.class);
                Bundle mBundle = new Bundle();
                mBundle.putParcelable("SpecificVacation", clickedVacation);
                specificVacation.putExtras(mBundle);
                startActivity(specificVacation);
            }
        });
    }

    private class VacationListAdapter extends ArrayAdapter<Vacation> {

        public VacationListAdapter() {
            super(getApplicationContext(), R.layout.vacation_item_view, vacationList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Haal de view op die ingevuld moet worden
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.vacation_item_view, parent, false);
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
            descTxt.setText(currentVacation.getDescription());
            TextView beginDateTxt = (TextView) itemView.findViewById(R.id.vacation_begindate_lbl);
            SimpleDateFormat formatter=new SimpleDateFormat("dd/MM/yyyy");
            beginDateTxt.setText(formatter.format(currentVacation.getBeginDate().getTime()));

            return itemView;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //overridePendingTransition(R.anim.slide_leave, R.anim.slide_enter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.vacation_overview, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchItem.getActionView();
        setupSearchView(searchItem);


//        Vervangt de action bar om de titel te centreren, maar de default "up" button verdwijnt. Ik laat centreren ff achterwege.
//        getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getActionBar().setCustomView(R.layout.actionbar);
        if( getActionBar() != null)
            getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case(android.R.id.home):
                finish();
                //overridePendingTransition(R.anim.slide_leave, R.anim.slide_enter);
                return true;
            case(R.id.action_filter):
                Intent filterOptions = new Intent(getApplicationContext(), VacationFilter.class);
                startActivityForResult(filterOptions, FILTER_OPTION_REQUEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        vacationAdapter.clear();
        populateVacationList();
        populateListView();
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

    private void setupSearchView(MenuItem searchItem) {
        searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            List<SearchableInfo> searchables = searchManager.getSearchablesInGlobalSearch();

            SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
            for (SearchableInfo inf : searchables) {
                if (inf.getSuggestAuthority() != null
                        && inf.getSuggestAuthority().startsWith("applications")) {
                    info = inf;
                }
            }
            mSearchView.setSearchableInfo(info);
        }
        mSearchView.setOnQueryTextListener(this);
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
            populateListView();
        }
        return false;
    }



}
