package com.example.opsc7312part1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class Sensor_data_fragment : Fragment(R.layout.sensor_data_fragment) {

    private lateinit var sensorDataAdapter : SensorDataAdapter
    private lateinit var sensorDataRecyclerView: RecyclerView
    private lateinit var sensorDataList: ArrayList<SensorData>

    lateinit var sensorTitle : Array<String>
    lateinit var sensorOptimalRange : Array<String>
    lateinit var sensorCurrentReading : Array<String>
    lateinit var sensorStatus : Array<String>

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }*/


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sensorDataInitialize()

        val layoutManager = LinearLayoutManager(context)
        sensorDataRecyclerView = view.findViewById(R.id.SensorDataRecyclerView)
        sensorDataRecyclerView.layoutManager = layoutManager
        sensorDataRecyclerView.setHasFixedSize(true)
        sensorDataAdapter = SensorDataAdapter(sensorDataList)
        sensorDataRecyclerView.adapter = sensorDataAdapter

        //Redirect to adjustment fragment
        sensorDataAdapter.setOnItemClickListener(object :SensorDataAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val selectedSensorData = sensorDataList[position]

                // Create a new instance of Sensor_adjustment_fragment with the selected data
                val sensorAdjFragment = Sensor_adjustment_fragment()
                // Replace the current fragment with Sensor_adjustment_fragment
                replaceFragment(sensorAdjFragment)

            }

        })

    }


    private fun sensorDataInitialize()
    {
        //hardcoded values
        sensorDataList = arrayListOf<SensorData>()

        sensorTitle = arrayOf(
            getString(R.string.head_1),
            getString(R.string.head_2),
            getString(R.string.head_3),
            getString(R.string.head_4),
        )

        sensorOptimalRange = arrayOf(
            getString(R.string.range_1),
            getString(R.string.range_2),
            getString(R.string.range_3),
            getString(R.string.range_4),
        )

        sensorCurrentReading = arrayOf(
            getString(R.string.reading_1),
            getString(R.string.reading_2),
            getString(R.string.reading_3),
            getString(R.string.reading_4),
        )

        sensorStatus = arrayOf(
            getString(R.string.status_1),
            getString(R.string.status_2),
            getString(R.string.status_3),
            getString(R.string.status_4),
        )

        for(i in sensorTitle.indices){
            val sensorData = SensorData(sensorTitle[i], sensorOptimalRange[i],sensorCurrentReading[i],sensorStatus[i].toBoolean())
            sensorDataList.add(sensorData)
        }


    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = parentFragmentManager // Use parentFragmentManager for fragments
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer, fragment)
        fragmentTransaction.addToBackStack(null) // Add the transaction to the back stack
        fragmentTransaction.commit()
    }



}

