package com.ang.acb.todolearn.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.ang.acb.todolearn.R

/**
 * The Settings screen. User can choose to receive notifications from this app or not.
 *
 * See: https://developer.android.com/guide/topics/ui/settings
 * See: https://codelabs.developers.google.com/codelabs/android-training-adding-settings-to-app
 */
class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // Inflate the XML resource
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }
}


