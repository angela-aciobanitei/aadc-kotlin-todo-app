package com.ang.acb.todolearn.ui.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ang.acb.todolearn.TasksApplication
import com.ang.acb.todolearn.databinding.StatisticsFragmentBinding
import com.ang.acb.todolearn.ui.common.ViewModelFactory


/**
 * Main UI for the statistics screen.
 */
class StatisticsFragment : Fragment() {

    private val viewModel: StatisticsViewModel by lazy {
        val factory = ViewModelFactory(
            (requireContext().applicationContext as TasksApplication).taskRepository
        )
        ViewModelProvider(this, factory).get(StatisticsViewModel::class.java)
    }

    private lateinit var binding : StatisticsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = StatisticsFragmentBinding.inflate(inflater,container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }
}
