package com.ang.acb.todolearn.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import com.ang.acb.todolearn.R
import com.ang.acb.todolearn.util.NightMode
import java.util.*

/**
 * The Settings screen. User can choose to receive notifications from this app or not,
 * and to change the app theme night mode: on, off, or auto.
 *
 * See: https://developer.android.com/guide/topics/ui/settings/use-saved-values
 */
class SettingsFragment : PreferenceFragmentCompat() {

    private val listener: SharedPreferences.OnSharedPreferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == getString(R.string.theme_pref_key)) {
                sharedPreferences.getString(
                    getString(R.string.theme_pref_key),
                    getString(R.string.theme_pref_entry_night_auto)
                )?.apply {
                    val mode = NightMode.valueOf(this.toUpperCase(Locale.US))
                    updateTheme(mode.value)
                }
            }
        }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // Inflate the XML resource
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    private fun updateTheme(nightMode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(nightMode)
        requireActivity().recreate()
        return true
    }

    override fun onStart() {
        super.onStart()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onStop() {
        super.onStop()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }
}


