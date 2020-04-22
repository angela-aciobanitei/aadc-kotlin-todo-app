package com.ang.acb.todolearn.ui.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

import com.ang.acb.todolearn.R
import com.ang.acb.todolearn.data.repo.TasksRepository
import com.ang.acb.todolearn.databinding.TasksFragmentBinding
import com.ang.acb.todolearn.ui.common.ViewModelFactory
import com.ang.acb.todolearn.util.EventObserver
import com.google.android.material.snackbar.Snackbar

class TasksFragment : Fragment() {

    private val  viewModel: TasksViewModel by lazy {
        val factory = ViewModelFactory(
            TasksRepository.getInstance(requireActivity().applicationContext)
        )
        ViewModelProvider(this, factory).get(TasksViewModel::class.java)
    }

    private val args: TasksFragmentArgs by navArgs()

    private lateinit var binding: TasksFragmentBinding
    private lateinit var tasksAdapter: TasksAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TasksFragmentBinding.inflate(inflater, container, false).apply {
            // Give DataBinding access to the view model.
            tasksViewModel = viewModel
            // Give DataBinding the possibility to observe LiveData.
            lifecycleOwner = viewLifecycleOwner
        }

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
            tasksResult?.let { tasksAdapter.submitList(it) }
        })
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

    private fun setupSnackbar() {
        viewModel.snackbarText.observe(viewLifecycleOwner, EventObserver { messageResId ->
            Snackbar.make(binding.root, messageResId, Snackbar.LENGTH_SHORT).show()
        })

        arguments?.let {
            viewModel.getResultMessage(args.snackbarMessage)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter_tasks_menu, menu)
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
