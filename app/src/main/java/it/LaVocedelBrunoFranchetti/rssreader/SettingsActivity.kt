package it.LaVocedelBrunoFranchetti.rssreader

import android.os.Bundle
import android.preference.PreferenceActivity
import it.LaVocedelBrunoFranchetti.rssreader.R.layout.activity_settings

class SettingsActivity: PreferenceActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_settings)
    }
}