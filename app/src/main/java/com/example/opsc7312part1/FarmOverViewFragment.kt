package com.example.opsc7312part1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FarmOverViewFragment : Fragment() {

    private lateinit var rVActions: RecyclerView
    private lateinit var rVData: RecyclerView
    private lateinit var btnActionsHeader: Button
    private lateinit var btnSensorDataHeader: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_farm_over_view, container, false)

        rVActions = view.findViewById(R.id.rVActions)
        rVData = view.findViewById(R.id.rVData)
        btnActionsHeader = view.findViewById(R.id.btnActionsHeader)
        btnSensorDataHeader = view.findViewById(R.id.btnSensorDataHeader)

        btnActionsHeader.setOnClickListener {
            rVActions.visibility = View.VISIBLE
            rVData.visibility = View.GONE
        }

        btnSensorDataHeader.setOnClickListener {
            rVData.visibility = View.VISIBLE
            rVActions.visibility = View.GONE
        }

        rVActions.layoutManager = LinearLayoutManager(requireContext())
        rVData.layoutManager = LinearLayoutManager(requireContext())

        val actionsRecyclerView: RecyclerView = view.findViewById(R.id.rVActions)
        val sensorDataRecyclerView: RecyclerView = view.findViewById(R.id.rVData)


        // Call the fetchActions method to get data from Firebase
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val actions = ActionUtils.fetchActions()
                val sensorData = FirebaseUtils.getSensorDataFromFirebase()

                // Convert List<Action> to ArrayList<Action>
                val actionsArrayList = ArrayList(actions)
                val sensorDataArrayList = ArrayList(sensorData)

                val eventAdapter = EventAdapter(actionsArrayList, requireContext())
                actionsRecyclerView.adapter = eventAdapter

                val dataAdapter = Data_Adapter(sensorDataArrayList, requireContext())
                sensorDataRecyclerView.adapter = dataAdapter
            } catch (e: Exception) {
                // Handle exception
                e.printStackTrace()
            }
        }

        return view
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            FarmOverViewFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}