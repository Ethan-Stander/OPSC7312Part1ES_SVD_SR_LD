package com.example.opsc7312part1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Button
import androidx.appcompat.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class FarmOverViewFragment : Fragment() {

    private lateinit var rVActions: RecyclerView
    private lateinit var rVData: RecyclerView
    private lateinit var btnActionsHeader: Button
    private lateinit var btnSensorDataHeader: Button

    private lateinit var searchActions: SearchView
    private lateinit var eventAdapter: EventAdapter
    private lateinit var dataAdapter: Data_Adapter
    private lateinit var actionsArrayList: ArrayList<Action>
    private lateinit var sensorDataArrayList: ArrayList<SensorDataAPISqlLite>


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

        (activity as AppCompatActivity).supportActionBar?.apply {
            setHasOptionsMenu(true)
        }

        rVActions = view.findViewById(R.id.rVActions)
        rVData = view.findViewById(R.id.rVData)
        btnActionsHeader = view.findViewById(R.id.btnActionsHeader)
        btnSensorDataHeader = view.findViewById(R.id.btnSensorDataHeader)
        searchActions = view.findViewById(R.id.searchActions)

        searchActions .setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })

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
                actionsArrayList = ArrayList(actions)
                sensorDataArrayList = ArrayList(sensorData)

                eventAdapter = EventAdapter(actionsArrayList, requireContext())
                actionsRecyclerView.adapter = eventAdapter

                dataAdapter = Data_Adapter(sensorDataArrayList, requireContext())
                sensorDataRecyclerView.adapter = dataAdapter
            } catch (e: Exception) {
                // Handle exception
                e.printStackTrace()
            }
        }

        return view
    }

    private fun filterList(query: String?) {
        if (query != null){

            var myActions = (arrayListOf <Action>())
            for(i in actionsArrayList){
                if(i.farmName.lowercase(Locale.ROOT).contains(query.lowercase())){
                    myActions.add(i)
                }
            }
            eventAdapter.setFilteredList(myActions)

            var mySensorData = (arrayListOf <SensorDataAPISqlLite>())
            for(i in sensorDataArrayList){
                if(i.farmName.lowercase(Locale.ROOT).contains(query.lowercase())){
                    mySensorData.add(i)
                }
            }
            dataAdapter.setFilteredList(mySensorData)
        }
    }

    //for info button in action bar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.action_refresh_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.RefreshOnly ->{

                FragmentUtils.refreshFragment(requireActivity(), FarmOverViewFragment(), R.id.frameLayout)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            FarmOverViewFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}