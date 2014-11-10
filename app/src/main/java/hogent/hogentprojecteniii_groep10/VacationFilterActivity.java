package hogent.hogentprojecteniii_groep10;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class VacationFilterActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new VacationFilterFragment())
                .commit();

    }

}
