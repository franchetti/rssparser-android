package it.LaVocedelBrunoFranchetti.rssreader

import android.os.Bundle
import android.preference.PreferenceActivity

class SettingsActivity: PreferenceActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO: Do this with a fragment.
        addPreferencesFromResource(R.xml.app_settings)
    }
}