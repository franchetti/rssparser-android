package it.LaVocedelBrunoFranchetti.rssreader

import android.os.Bundle
import android.preference.PreferenceActivity

class SettingsActivity: PreferenceActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: Do this with a fragment and remove deprecated usage.
        addPreferencesFromResource(R.xml.app_settings)
    }
}