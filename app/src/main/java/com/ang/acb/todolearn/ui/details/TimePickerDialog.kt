package com.ang.acb.todolearn.ui.details

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.ang.acb.todolearn.TasksApplication
import com.ang.acb.todolearn.ui.common.ViewModelFactory
import java.util.*

/**
 * A simple time picker dialog for setting the [Task] deadline.
 *
 * See: https://developer.android.com/guide/topics/ui/controls/pickers#TimePicker
 */
class TimePickerDialog: DialogFragment(), TimePickerDialog.OnTimeSetListener {

    private val viewModel: AddEditTaskViewModel by lazy {
        val factory = ViewModelFactory(
            (requireContext().applicationContext as TasksApplication).taskRepository
        )
        ViewModelProvider(requireActivity(), factory).get(AddEditTaskViewModel::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current time as the default values for the picker
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        viewModel.setDeadline(hourOfDay, minute)
    }
}