package com.ang.acb.todolearn.ui.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.ang.acb.todolearn.data.repo.TasksRepository

import com.ang.acb.todolearn.databinding.AddEditTaskFragmentBinding
import com.ang.acb.todolearn.ui.common.ADD_RESULT_OK
import com.ang.acb.todolearn.ui.common.ViewModelFactory
import com.ang.acb.todolearn.util.EventObserver
import com.google.android.material.snackbar.Snackbar

class AddEditTaskFragment : Fragment() {

    private lateinit var binding: AddEditTaskFragmentBinding

    private val args : AddEditTaskFragmentArgs by navArgs()

    private val viewModel: AddEditTaskViewModel by lazy {
        val factory = ViewModelFactory(
            TasksRepository.getInstance(requireActivity().applicationContext)
        )
        ViewModelProvider(this, factory).get(AddEditTaskViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddEditTaskFragmentBinding.inflate(inflater, container, false).apply {
            // Give DataBinding access to the view model.
            addEditTasksViewModel = viewModel
            // Give DataBinding the possibility to observe LiveData.
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.start(args.taskId)

        setupSnackbar()
        setupNavigation()
    }

    private fun setupSnackbar() {
        viewModel.snackbarText.observe(viewLifecycleOwner, EventObserver { stringResId ->
            Snackbar.make(binding.root, stringResId, Snackbar.LENGTH_SHORT).show()
        })
    }

    private fun setupNavigation() {
        viewModel.taskUpdatedEvent.observe(viewLifecycleOwner, EventObserver {
            val action = AddEditTaskFragmentDirections
                .actionAddEditTaskFragmentToTasksFragment(ADD_RESULT_OK)
            findNavController().navigate(action)
        })
    }
}
