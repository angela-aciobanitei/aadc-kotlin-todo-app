package com.ang.acb.todolearn.ui.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ang.acb.todolearn.R
import kotlinx.android.synthetic.main.pie_fragment.*

class PieFragment : Fragment() {

    private lateinit var viewModel: PieViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pie_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PieViewModel::class.java)

        pieChart.setData(viewModel.data)
    }

}
