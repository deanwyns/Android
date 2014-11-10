package hogent.hogentprojecteniii_groep10.activities;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import hogent.hogentprojecteniii_groep10.preferences.VacationFilterFragment;

public class VacationFilterActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new VacationFilterFragment())
                .commit();

    }

}
