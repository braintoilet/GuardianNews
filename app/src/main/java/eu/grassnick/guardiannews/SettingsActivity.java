package eu.grassnick.guardiannews;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class NewsPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings_main);

            Preference keyword = findPreference(getString(R.string.settings_keyword_key));
            Preference pageSize = findPreference(getString(R.string.settings_pagesize_key));
            Preference orderBy = findPreference(getString(R.string.settings_order_by_key));

            bindSummaryToValue(keyword);
            bindSummaryToValue(pageSize);
            bindSummaryToValue(orderBy);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            preference.setSummary(value.toString());
            return true;
        }

        private void bindSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);

            //get default PreferenceManager, then call onPreferenceChange to set the summary
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            onPreferenceChange(preference, preferences.getString(preference.getKey(), ""));
        }
    }
}
