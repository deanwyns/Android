package hogent.hogentprojecteniii_groep10.preferences;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import hogent.hogentprojecteniii_groep10.R;

public class VacationFilterFragment extends PreferenceFragment {
    private CheckBox ageFilterCheckbox;
    private TextView startAgeTxt, endAgeTxt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(getActivity().getApplicationContext(), R.xml.vacation_filter_settings, false);
        addPreferencesFromResource(R.xml.vacation_filter_settings);

        ageFilterCheckbox = (CheckBox) getActivity().findViewById(R.id.age_filter_checkbox);
        startAgeTxt = (TextView) getActivity().findViewById(R.id.start_age_input_txt);
        endAgeTxt = (TextView) getActivity().findViewById(R.id.end_age_input_txt);
    }

    @Override
    public void onPause() {
        super.onPause();

    }
}
