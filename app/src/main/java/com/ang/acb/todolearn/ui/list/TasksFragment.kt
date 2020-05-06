package com.ang.acb.todolearn.ui.list

import android.app.NotificationManager
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import com.ang.acb.todolearn.R
import com.ang.acb.todolearn.TasksApplication
import com.ang.acb.todolearn.data.local.Task
import com.ang.acb.todolearn.databinding.TasksFragmentBinding
import com.ang.acb.todolearn.util.EventObserver
import com.ang.acb.todolearn.ui.common.ViewModelFactory
import com.google.android.material.snackbar.Snackbar


/**
 * Displays a list of [Task] items. Users can choose to view all, active or completed tasks.
 */
class TasksFragment : Fragment() {

    private val  viewModel: TasksViewModel by lazy {
        val app = requireContext().applicationContext as TasksApplication
        val factory = ViewModelFactory(app.taskRepository)
        ViewModelProvider(this, factory).get(TasksViewModel::class.java)
    }

    private val args: TasksFragmentArgs by navArgs()

    private lateinit var binding: TasksFragmentBinding
    private lateinit var tasksAdapter: TasksAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TasksFragmentBinding.inflate(inflater, container, false)
        // Give DataBinding access to the view model.
        binding.tasksViewModel = viewModel
        // Give DataBinding the possibility to observe LiveData.
        binding.lifecycleOwner = viewLifecycleOwner

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupRecycler()
        setupSnackbar()
        setupNavigation()
    }

    private fun setupRecycler() {
        tasksAdapter = TasksAdapter(viewModel)
        binding.rvTasks.adapter = tasksAdapter

        viewModel.tasks.observe(viewLifecycleOwner, Observer { tasksResult ->
            tasksResult?.let(tasksAdapter::submitList)
        })
    }

    private fun setupSnackbar() {
        viewModel.snackbarText.observe(viewLifecycleOwner, EventObserver { messageResId ->
            Snackbar.make(binding.root, messageResId, Snackbar.LENGTH_SHORT).show()
        })

        arguments?.let { viewModel.getResultMessage(args.snackbarMessage) }
    }

    private fun setupNavigation() {
        viewModel.newTaskEvent.observe(viewLifecycleOwner, EventObserver {
            val action = TasksFragmentDirections
                .actionTasksFragmentToAddEditTaskFragment(
                    taskId = null,
                    title = resources.getString(R.string.new_task)
                )
            findNavController().navigate(action)
        })

        viewModel.openTaskDetails.observe(viewLifecycleOwner, EventObserver { taskId ->
            val action = TasksFragmentDirections
                .actionTasksFragmentToTaskDetailsFragment(taskId)
            findNavController().navigate(action)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.tasks_list_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    // https://developer.android.com/guide/topics/ui/menus#kotlin
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.show_all_tasks ->  {
                viewModel.updateFilter(TasksFilter.ALL_TASKS)
                true
            }
            R.id.show_active_tasks -> {
                viewModel.updateFilter(TasksFilter.ACTIVE_TASKS)
                true
            }
            R.id.show_completed_tasks -> {
                viewModel.updateFilter(TasksFilter.COMPLETED_TASKS)
                true
            }
            R.id.clear_all_tasks -> {
                viewModel.clearAllTasks()
                true
            }
            R.id.clear_completed -> {
                viewModel.clearCompletedTasks()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
