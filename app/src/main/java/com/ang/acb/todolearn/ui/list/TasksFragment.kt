package com.ang.acb.todolearn.ui.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.ang.acb.todolearn.R
import com.ang.acb.todolearn.data.local.TasksRepository
import com.ang.acb.todolearn.databinding.TasksFragmentBinding
import com.ang.acb.todolearn.ui.common.ViewModelFactory
import com.ang.acb.todolearn.util.EventObserver

class TasksFragment : Fragment() {

    private val  viewModel: TasksViewModel by lazy {
        val factory = ViewModelFactory(
            TasksRepository.getInstance(requireActivity().applicationContext)
        )
        ViewModelProvider(this, factory).get(TasksViewModel::class.java)
    }

    private lateinit var binding: TasksFragmentBinding

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

        viewModel.navigateToAddTask.observe(viewLifecycleOwner, EventObserver {
            val action = TasksFragmentDirections
                .actionTasksFragmentToAddEditTaskFragment(
                    taskId = -1,
                    title = resources.getString(R.string.new_task)
                )
            findNavController().navigate(action)
        })
    }
}
