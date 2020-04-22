package com.ang.acb.todolearn.ui.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView

import com.ang.acb.todolearn.R
import com.ang.acb.todolearn.data.local.Result
import com.ang.acb.todolearn.data.repo.TasksRepository
import com.ang.acb.todolearn.databinding.TasksFragmentBinding
import com.ang.acb.todolearn.ui.common.ViewModelFactory
import com.ang.acb.todolearn.util.EventObserver
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

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
        binding.rvTasks.addItemDecoration(DividerItemDecoration(requireContext(), HORIZONTAL))

        viewModel.tasks.observe(viewLifecycleOwner, Observer { tasksResult ->
            when (tasksResult) {
                is Result.Success -> {
                    tasksAdapter.submitList(tasksResult.data)
                }
                is Result.Error -> {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.error_loading_tasks_message),
                        Snackbar.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun setupNavigation() {
        viewModel.navigateToAddTask.observe(viewLifecycleOwner, EventObserver {
            val action = TasksFragmentDirections
                .actionTasksFragmentToAddEditTaskFragment(
                    taskId = null,
                    title = resources.getString(R.string.new_task)
                )
            findNavController().navigate(action)
        })
    }

    private fun setupSnackbar() {
        arguments?.let {
            viewModel.getResultMessage(args.snackbarMessage)
        }
        viewModel.snackbarText.observe(viewLifecycleOwner, EventObserver { stringResId ->
            Snackbar.make(binding.root, stringResId, Snackbar.LENGTH_SHORT).show()
        })
    }
}
