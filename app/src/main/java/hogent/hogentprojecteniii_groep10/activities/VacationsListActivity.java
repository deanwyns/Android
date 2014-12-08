package hogent.hogentprojecteniii_groep10.activities;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.SearchView;

import java.util.List;

import hogent.hogentprojecteniii_groep10.R;
import hogent.hogentprojecteniii_groep10.fragments.VacationDetailFragment;
import hogent.hogentprojecteniii_groep10.fragments.VacationsListFragment;
import hogent.hogentprojecteniii_groep10.models.Vacation;

/**
 * De activity die een lijst van vakanties zal tonen.
 * Wordt opgevuld met een fragment (VaationListFragment).
 * Onderdeel van het master/detail overzicht.
 */
public class VacationsListActivity extends FragmentActivity implements VacationsListFragment.OnListItemSelectedListener {
    private boolean isTwoPane = false;
    private SearchView mSearchView;
    private VacationsListFragment fragmentItemsList;
    public static final int FILTER_OPTION_REQUEST = 1;

    /**
     * Zal de activity opvullen met het acivity_vacations_list xml bestand.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacations_list);
        determinePaneLayout();
    }

    /**
     * Bepaald of het een tablet of een smartphone is.
     * Zal het itemListFragment al initialiseren.
     */
    private void determinePaneLayout() {
        FrameLayout fragmentItemDetail = (FrameLayout) findViewById(R.id.flDetailContainer);
        if (fragmentItemDetail != null) {
            isTwoPane = true;
        }
        fragmentItemsList = (VacationsListFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentVacationsList);
    }

    /**
     * Zal het menu aanmaken van deze activity.
     * @param menu het menu dat wordt aangepast
     * @return bepaald hoe verdere menu processing wordt afgehandeld
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.vacation_overview, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) searchItem.getActionView();
        setupSearchView(searchItem);
        if( getActionBar() != null)
            getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        return true;
    }

    /**
     * Bepaald de actie die wordt uitgevoerd bij het uitvoeren van een menu item
     * @param item het item waarop geklikt is
     * @return bepaald hoe verdere menu processing wordt afgehandeld
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case(R.id.action_settings):
                Intent settings = new Intent(getApplicationContext(), MainSettingsActivity.class);
                startActivity(settings);
                return true;
            case(android.R.id.home):
                finish();
                //overridePendingTransition(R.anim.slide_leave, R.anim.slide_enter);
                return true;
            case(R.id.action_filter):
                Intent filterOptions = new Intent(getApplicationContext(), VacationFilterActivity.class);
                fragmentItemsList.startActivityForResult(filterOptions, FILTER_OPTION_REQUEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Afhankelijk van of het een tablet is, zal het detail venster getoond worden.
     * Indien het geen tablet is, zal de activity gestart worden bij het klikken op een vakantie.
     * @param vacation de vakantie waarop geklikt is
     */
    @Override
    public void onItemSelected(Vacation vacation) {
        if (isTwoPane) { // single activity with list and detail
            // Replace framelayout with new detail fragment
            VacationDetailFragment fragmentItem = VacationDetailFragment.newInstance(vacation);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flDetailContainer, fragmentItem);
            ft.commit();
        } else {
            // For phone, launch detail activity using intent
            Intent i = new Intent(this, VacationDetailActivity.class);
            // Embed the serialized item
            i.putExtra("SpecificVacation", vacation);
            // Start the activity
            startActivity(i);
        }
    }

    /**
     * Methode om te kunnen zoeken in de lijst van vakanties.
     * Standaard implementatie zoals in de documentatie.
     * @param searchItem het icoon dat search toont
     */
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
        mSearchView.setOnQueryTextListener(fragmentItemsList);
    }
}
