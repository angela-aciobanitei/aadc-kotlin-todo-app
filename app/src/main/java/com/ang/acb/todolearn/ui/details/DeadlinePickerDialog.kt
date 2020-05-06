package com.ang.acb.todolearn.ui.details

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.DialogFragment
import java.util.*

/**
 * A simple time picker dialog for setting the [Task] deadline.
 *
 * See: https://developer.android.com/guide/topics/ui/controls/pickers#TimePicker
 */
class DeadlinePickerDialog: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current time as the default values for the picker
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        // Create a new instance of DeadlinePickerDialog and return it
        return TimePickerDialog(
            activity,
            parentFragment as TimePickerDialog.OnTimeSetListener,
            hour,
            minute,
            DateFormat.is24HourFormat(activity)
        )
    }
}