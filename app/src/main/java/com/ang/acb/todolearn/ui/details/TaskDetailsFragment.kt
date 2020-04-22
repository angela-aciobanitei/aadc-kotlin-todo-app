package com.ang.acb.todolearn.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ang.acb.todolearn.R
import com.ang.acb.todolearn.data.repo.TasksRepository
import com.ang.acb.todolearn.databinding.TaskDetailsFragmentBinding
import com.ang.acb.todolearn.ui.common.ViewModelFactory
import com.ang.acb.todolearn.util.EventObserver
import com.google.android.material.snackbar.Snackbar

class TaskDetailsFragment : Fragment() {

    private val viewModel: TaskDetailsViewModel by lazy {
        val factory = ViewModelFactory(
            TasksRepository.getInstance(requireContext().applicationContext)
        )
        ViewModelProvider(this, factory).get(TaskDetailsViewModel::class.java)
    }

    private val args: TaskDetailsFragmentArgs by navArgs()

    private lateinit var binding: TaskDetailsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TaskDetailsFragmentBinding.inflate(inflater, container, false).apply {
            taskDetailsViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        
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
    }

    private fun populateUi() {
        viewModel.task.observe(viewLifecycleOwner, Observer { task ->
            task?.let {
                binding.task = it
            }
        })
    }

}
