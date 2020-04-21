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
import com.ang.acb.todolearn.databinding.TasksFragmentBinding

class TasksFragment : Fragment() {

    private lateinit var viewModel: TasksViewModel
    private lateinit var binding: TasksFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TasksFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TasksViewModel::class.java)
        binding.tasksViewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner

        binding.addNewTaskFab.setOnClickListener{
            viewModel.navigateToAddTask()
        }

        viewModel.navigateToAddTask.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val action = TasksFragmentDirections
                    .actionTasksFragmentToAddEditTaskFragment(
                        taskId = -1,
                        title = resources.getString(R.string.new_task)
                    )
                findNavController().navigate(action)
                viewModel.navigateToAddTaskCompleted()
            }
        })
    }
}
