package hogent.hogentprojecteniii_groep10.fragments;



import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import hogent.hogentprojecteniii_groep10.R;

/**
 * Het fragment voor main settings
 */
public class MainSettingsFragment extends PreferenceFragment {

    /**
     * Oncreate zal de preferences opvullen op basis van het xml bestand main_settings
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.main_settings);
    }


}
