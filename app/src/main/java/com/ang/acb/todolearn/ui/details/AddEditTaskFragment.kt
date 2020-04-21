package com.ang.acb.todolearn.ui.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ang.acb.todolearn.data.local.TasksRepository

import com.ang.acb.todolearn.databinding.AddEditTaskFragmentBinding
import com.ang.acb.todolearn.ui.common.ViewModelFactory

class AddEditTaskFragment : Fragment() {

    private lateinit var binding: AddEditTaskFragmentBinding

    private val args : AddEditTaskFragmentArgs by navArgs()

    private val viewModel: AddEditTaskViewModel by lazy {
        val factory = ViewModelFactory(TasksRepository.getInstance(requireActivity().applicationContext))
        ViewModelProvider(this, factory).get(AddEditTaskViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddEditTaskFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.addEditTasksViewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner

        viewModel.start(args.taskId, args.title)

        binding.saveTaskFab.setOnClickListener {

        }

        viewModel.taskUpdated.observe(viewLifecycleOwner, Observer {
            val action = AddEditTaskFragmentDirections.actionAddEditTaskFragmentToTasksFragment()
            findNavController().navigate(action)

        })
    }

}
