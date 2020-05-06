package com.ang.acb.todolearn.ui.details

import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ang.acb.todolearn.TasksApplication
import com.ang.acb.todolearn.data.local.Task
import com.ang.acb.todolearn.databinding.AddEditTaskFragmentBinding
import com.ang.acb.todolearn.ui.common.ADD_EDIT_RESULT_OK
import com.ang.acb.todolearn.ui.common.ViewModelFactory
import com.ang.acb.todolearn.util.EventObserver
import com.google.android.material.snackbar.Snackbar


/**
 * Main screen for adding or editing a [Task] item.
 */
class AddEditTaskFragment : Fragment(), TimePickerDialog.OnTimeSetListener {

    private val args : AddEditTaskFragmentArgs by navArgs()

    private val viewModel: AddEditTaskViewModel by lazy {
        val app = requireContext().applicationContext as TasksApplication
        val factory = ViewModelFactory(app.taskRepository)
        ViewModelProvider(this, factory).get(AddEditTaskViewModel::class.java)
    }

    private lateinit var binding: AddEditTaskFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddEditTaskFragmentBinding.inflate(inflater, container, false)
        // Give DataBinding access to the view model.
        binding.addEditTasksViewModel = viewModel
        // Give DataBinding the possibility to observe LiveData.
        binding.lifecycleOwner = viewLifecycleOwner


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.start(args.taskId)

        setupSnackbar()
        setupNavigation()
        setTaskDeadline()
    }

    private fun setupSnackbar() {
        viewModel.snackbarText.observe(viewLifecycleOwner, EventObserver { stringResId ->
            Snackbar.make(binding.root, stringResId, Snackbar.LENGTH_SHORT).show()
        })
    }

    private fun setupNavigation() {
        viewModel.taskUpdatedEvent.observe(viewLifecycleOwner, EventObserver {
            val action = AddEditTaskFragmentDirections
                .actionAddEditTaskFragmentToTasksFragment(ADD_EDIT_RESULT_OK)
            findNavController().navigate(action)
        })
    }

    private fun setTaskDeadline() {
        viewModel.taskDeadlineEvent.observe(viewLifecycleOwner, EventObserver {
            DeadlinePickerDialog().show(childFragmentManager, "TIME_PICKER")
        })
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        viewModel.setDeadline(hourOfDay, minute)
    }
}
