package com.ang.acb.todolearn.ui.statistics

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider

import com.ang.acb.todolearn.R
import com.ang.acb.todolearn.data.repo.TasksRepository
import com.ang.acb.todolearn.databinding.StatisticsFragmentBinding
import com.ang.acb.todolearn.ui.common.ViewModelFactory

class StatisticsFragment : Fragment() {

    private val viewModel: StatisticsViewModel by lazy {
        val factory = ViewModelFactory(
            TasksRepository.getInstance(requireActivity().applicationContext)
        )
        ViewModelProvider(this, factory).get(StatisticsViewModel::class.java)
    }

    private lateinit var binding : StatisticsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = StatisticsFragmentBinding.inflate(inflater,container, false)
        binding.statsViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }
}
