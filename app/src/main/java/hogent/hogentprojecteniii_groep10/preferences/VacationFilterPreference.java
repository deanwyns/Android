package hogent.hogentprojecteniii_groep10.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import hogent.hogentprojecteniii_groep10.R;

public class VacationFilterPreference extends DialogPreference {
    private CheckBox ageFilterCheckbox;
    private TextView startAgeTxt, endAgeTxt;

    public VacationFilterPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutResource(R.layout.age_filter_dialog);

        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
        setDialogIcon(null);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        SharedPreferences sharedPreferences = getSharedPreferences();
        startAgeTxt.setText(sharedPreferences.getInt(getContext().getResources().getString(R.string.start_age_filter_key), 0));
        endAgeTxt.setText(sharedPreferences.getInt(getContext().getResources().getString(R.string.end_age_filter_key), 30));
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            SharedPreferences.Editor editor = getEditor();
            editor.putInt(getContext().getResources().getString(R.string.start_age_filter_key), Integer.parseInt(startAgeTxt.getText().toString()));
            editor.putInt(getContext().getResources().getString(R.string.end_age_filter_key), Integer.parseInt(endAgeTxt.getText().toString()));
            editor.commit();
        }
    }
}
