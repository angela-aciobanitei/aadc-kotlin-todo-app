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

import com.ang.acb.todolearn.databinding.AddEditTaskFragmentBinding

class AddEditTaskFragment : Fragment() {

    private lateinit var binding: AddEditTaskFragmentBinding
    private lateinit var viewModel: AddEditTaskViewModel

    private val args : AddEditTaskFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddEditTaskFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddEditTaskViewModel::class.java)
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
