package com.ang.acb.todolearn.ui.statistics

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ang.acb.todolearn.R
import com.ang.acb.todolearn.data.repo.TasksRepository
import com.ang.acb.todolearn.ui.common.ViewModelFactory
import kotlinx.android.synthetic.main.pie_fragment.*

class PieFragment : Fragment() {

    private val viewModel: PieViewModel by lazy {
        val factory = ViewModelFactory(
            TasksRepository.getInstance(requireActivity().applicationContext)
        )
        ViewModelProvider(this, factory).get(PieViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pie_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.stats.observe(viewLifecycleOwner, Observer { statsResult ->
            statsResult?.let {
                val pieData = PieData()
                pieData.add("active", it.activeTasksPercent.toDouble(), "#FF4500")
                pieData.add("completed", it.completedTasksPercent.toDouble(), "#03DAC5")
                pieChart.setData(pieData)
            }
        })
    }

}
