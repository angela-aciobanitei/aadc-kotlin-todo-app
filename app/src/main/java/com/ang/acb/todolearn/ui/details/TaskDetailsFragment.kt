package com.ang.acb.todolearn.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.ang.acb.todolearn.R
import com.ang.acb.todolearn.data.repo.TasksRepository
import com.ang.acb.todolearn.databinding.TaskDetailsFragmentBinding
import com.ang.acb.todolearn.ui.common.ViewModelFactory

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

    }

}
