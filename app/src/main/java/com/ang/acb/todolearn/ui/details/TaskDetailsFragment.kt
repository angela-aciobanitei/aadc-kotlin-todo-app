package com.ang.acb.todolearn.ui.details

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ang.acb.todolearn.R
import com.ang.acb.todolearn.TasksApplication
import com.ang.acb.todolearn.data.local.Task
import com.ang.acb.todolearn.databinding.TaskDetailsFragmentBinding
import com.ang.acb.todolearn.ui.common.DELETE_RESULT_OK
import com.ang.acb.todolearn.ui.common.ViewModelFactory
import com.ang.acb.todolearn.util.EventObserver
import com.google.android.material.snackbar.Snackbar


/**
 * Main screen for displaying the [Task] details.
 */
class TaskDetailsFragment : Fragment() {

    private val args: TaskDetailsFragmentArgs by navArgs()

    private val viewModel: TaskDetailsViewModel by lazy {
        val app = requireContext().applicationContext as TasksApplication
        val factory = ViewModelFactory(app.taskRepository)
        ViewModelProvider(this, factory).get(TaskDetailsViewModel::class.java)
    }

    private lateinit var binding: TaskDetailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TaskDetailsFragmentBinding.inflate(inflater, container, false).apply {
            taskDetailsViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.start(args.taskId)
        setupSnackbar()
        setupNavigation()
        populateUi()
    }

    private fun setupSnackbar() {
        viewModel.snackbarText.observe(viewLifecycleOwner, EventObserver { messageResId ->
            Snackbar.make(binding.root, messageResId, Snackbar.LENGTH_SHORT).show()
        })
    }

    private fun setupNavigation() {
        viewModel.editTaskEvent.observe(viewLifecycleOwner, EventObserver { taskId ->
            val action = TaskDetailsFragmentDirections
                .actionTaskDetailsFragmentToAddEditTaskFragment(
                    taskId = taskId,
                    title = resources.getString(R.string.edit_task)
                )
            findNavController().navigate(action)
        })

        viewModel.deleteTaskEvent.observe(viewLifecycleOwner, Observer {
            val action = TaskDetailsFragmentDirections
                .actionTaskDetailsFragmentToTasksFragment(DELETE_RESULT_OK)
            findNavController().navigate(action)
        })
    }

    private fun populateUi() {
        viewModel.task.observe(viewLifecycleOwner, Observer { task ->
            task?.let {
                binding.task = it
            }
        })
    }

    private fun showConfirmDeletionDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        // https://developer.android.com/guide/topics/ui/dialogs
        val builder = AlertDialog.Builder(requireContext())
        builder.apply {
            setMessage(R.string.delete_task_confirmation_message)
            setPositiveButton(R.string.ok) { dialog, id ->
                viewModel.deleteTask()
            }
            setNegativeButton(R.string.cancel) { dialog, id ->
                dialog?.dismiss()
            }
        }

        // Create the AlertDialog
        val alertDialog = builder.create()

        alertDialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.task_details_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_task -> {
                showConfirmDeletionDialog()
                true
            }
            else ->  super.onOptionsItemSelected(item)
        }
    }
}
