package com.ang.acb.todolearn.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat

import com.ang.acb.todolearn.R

/**
 * See: https://developer.android.com/guide/topics/ui/settings
 * See: https://codelabs.developers.google.com/codelabs/android-training-adding-settings-to-app
 */
class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // Inflate the XML resource
        setPreferencesFromResource(R.xml.preferences, rootKey)

        // Get the notification switch preference
        val switchDarkMode: SwitchPreferenceCompat? = findPreference(
            getString(R.string.show_notifications_pref_key)
        )

        // Switch preference change listener
        switchDarkMode?.setOnPreferenceChangeListener{ preference, newValue ->
            if (newValue == true){
                Toast.makeText(activity,"enabled",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(activity,"disabled",Toast.LENGTH_LONG).show()
            }

            true
        }
    }
}


