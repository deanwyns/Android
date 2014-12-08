package hogent.hogentprojecteniii_groep10.activities;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.Menu;
import android.view.MenuItem;
import hogent.hogentprojecteniii_groep10.R;
import hogent.hogentprojecteniii_groep10.fragments.MainSettingsFragment;

/**
 * De activity die de settings fragment zal tonen
 */
public class MainSettingsActivity extends Activity {
    //De key die wordt bijgehouden in de XML waarmee de waarde kan opgevraagd worden
    public static final String LOGGED_IN_AS_CHILD = "pref_key_logged_in_as_child";

    /**
     * Zal de content vervangen door het MainSettings fragment.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainSettingsFragment()).commit();
    }

    /**
     * Verwijdert het icoon van de activity
     * @param menu het menu dat wordt aangepast
     * @return bepaald hoe verdere menu processing wordt afgehandeld
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if( getActionBar() != null)
            getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        return true;
    }
}
