package com.ang.acb.todolearn.ui.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ang.acb.todolearn.R
import com.ang.acb.todolearn.TasksApplication
import com.ang.acb.todolearn.ui.common.ViewModelFactory
import kotlinx.android.synthetic.main.pie_fragment.*


/**
 * Displays tasks statistics (percentage of active vs completed tasks)
 * using a custom view, [PieChart].
 */
class PieFragment : Fragment() {

    private val viewModel: PieViewModel by lazy {
        val factory = ViewModelFactory(
            (requireContext().applicationContext as TasksApplication).taskRepository
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
            if (statsResult == null || statsResult == StatsResult(0f, 0f)) {
                val noData = PieData().apply {
                    add("no tasks", 100.toDouble(), "#757575" )
                }
                pieChart.setData(noData)
            } else {
                val pieData = PieData()
                if (statsResult.activeTasksPercent != 0f) {
                    pieData.add("active", statsResult.activeTasksPercent.toDouble(), "#FF4500")
                }
                if (statsResult.completedTasksPercent != 0f) {
                    pieData.add("completed", statsResult.completedTasksPercent.toDouble(), "#03DAC5")
                }
                pieChart.setData(pieData)
            }
        })
    }
}
