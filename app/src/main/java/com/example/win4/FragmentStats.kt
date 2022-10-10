package com.example.win4

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.win4.databinding.FragmentStatsBinding
import com.example.win4.model.BetModel
import com.example.win4.room.AppDatabase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class FragmentStats : Fragment() {

    companion object {
        @JvmStatic
        fun newInstance() = FragmentStats()
    }

    private lateinit var binding: FragmentStatsBinding
    private lateinit var appDatabase: AppDatabase
    var betModelList: ArrayList<BetModel> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appDatabase = AppDatabase.getDatabase(requireContext())


        GlobalScope.launch {
            betModelList = appDatabase.betDao().getAll() as ArrayList<BetModel>
            if (betModelList.isNotEmpty()) {
                launch(Dispatchers.Main) {
                    createChart(betModelList)
                }
            }
        }
    }

    private fun createChart(betModelList: ArrayList<BetModel>) {
        val lineChart = binding.lineChart
        val chartList = ArrayList<Entry>()
        for (i in betModelList.indices) {
            chartList.add(Entry(i.toFloat(), betModelList[i].capital.toFloat()))
        }
        chartList.add(Entry(betModelList.size.toFloat(), getCapitalBankroll()!!.toFloat()))
        val lineDataset = LineDataSet(chartList, "")
        lineDataset.lineWidth = 4f
        lineDataset.color = Color.RED
        lineDataset.setDrawCircles(true)
        lineDataset.setDrawCircleHole(false)
        val arrayIline = ArrayList<ILineDataSet>()
        arrayIline.add(lineDataset)
        val lineData = LineData(arrayIline)
        lineChart.data = lineData
        lineChart.invalidate()
    }


    private fun getCapitalBankroll(): String? {
        val sharedPrefBankroll =
            requireContext().getSharedPreferences("bankrollPref", Context.MODE_PRIVATE)
        return sharedPrefBankroll.getString("capital", "")
    }


}