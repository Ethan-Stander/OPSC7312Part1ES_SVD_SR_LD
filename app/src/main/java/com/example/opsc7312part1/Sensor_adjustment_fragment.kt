package com.example.opsc7312part1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.opsc7312part1.APIServices.Companion.fetchSensorDataFromJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.Text


class Sensor_adjustment_fragment : Fragment(R.layout.sensor_adjustment_fragment) {


private lateinit var btnAPi : Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.sensor_adjustment_fragment, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnAPi = requireView().findViewById(R.id.btnAPItester)

        btnAPi.setOnClickListener {
            val scope = CoroutineScope(Dispatchers.Main)
            val job = scope.launch {
                val sensorData: SensorDataAPI? = fetchSensorDataFromJson()

                if (sensorData != null) {
                    // Do something with the fetched sensor data
                    Log.i("testing API","Fetched sensor data: $sensorData")
                } else {
                    Log.i("testing API","failed to Fetch sensor data: $sensorData")
                }
            }

        }

    }



    }
