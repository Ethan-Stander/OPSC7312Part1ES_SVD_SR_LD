package com.example.opsc7312part1

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class Sensor_data_fragment :Fragment(R.layout.sensor_data_fragment) {

    private lateinit var sensorDataAdapter : SensorDataAdapter
    private lateinit var sensorDataRecyclerView: RecyclerView
    private lateinit var sensorDataList: ArrayList<SensorData>

    lateinit var sensorTitle : Array<String>
    lateinit var sensorOptimalRange : Array<String>
    lateinit var sensorCurrentReading : Array<String>
    lateinit var sensorStatus : Array<String>

    lateinit var attributeNames : ArrayList<String>
    lateinit var readings : ArrayList<String>

    //for pop up info
    private lateinit var infoDataAdapter: InfoDataAdapter
    private lateinit var popupRecyclerView: RecyclerView
    private lateinit var infoDataList :ArrayList<InfoData>

    private lateinit var popupWindow: PopupWindow
    private lateinit var popupView: View

    private lateinit var tvSensorErrorMessage: TextView

    private lateinit var sensorLoadingBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //info pop up
        popupView = LayoutInflater.from(context).inflate(R.layout.info_pop_ups,null)
        popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true)

        //exit button on pop up
        val exitButton :ImageButton = popupView.findViewById(R.id.exitButton)
        exitButton.setOnClickListener {
            popupWindow.dismiss()

        }

        sensorDataRecyclerView = view.findViewById(R.id.SensorDataRecyclerView)

        sensorLoadingBar = view.findViewById(R.id.sensorLoadingBar)

        tvSensorErrorMessage = view.findViewById(R.id.tvSensorErrorMessage)

        val scope = CoroutineScope(Dispatchers.Main)
        val job = scope.launch {
            showLoading() // Show loading bar
            delay(1000)
            if (sensorDataInitialize()) {
                // Data loaded successfully, hide error message
                hideError()
            } else {
                // Data loading failed, show error message
                showError("Error loading sensor data...\n(please reconnect)")
            }

            val gridLayoutManager = GridLayoutManager(context,2)
            sensorDataRecyclerView = view.findViewById(R.id.SensorDataRecyclerView)
            sensorDataRecyclerView.layoutManager = gridLayoutManager
            sensorDataRecyclerView.setHasFixedSize(true)
            sensorDataAdapter = SensorDataAdapter(sensorDataList)
            sensorDataRecyclerView.adapter = sensorDataAdapter

            // Redirect to adjustment fragment
            sensorDataAdapter.setOnItemClickListener(object : SensorDataAdapter.onItemClickListener {
                override fun onItemClick(position: Int) {
//                    val sensorTitle = sensorTitle[position]
//                    val sensorAdjFragment = Sensor_adjustment_fragment()
//                    replaceFragment(sensorAdjFragment)
//                    (activity as AppCompatActivity).title = "$sensorTitle : Sensor Adjustment"
                }
            })
            hideLoading()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.sensor_data_fragment, container, false)
        (activity as AppCompatActivity).supportActionBar?.apply {
            setHasOptionsMenu(true)
        }
        return rootView
    }

    //for info button in action bar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.action_bar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.info -> {
                // Handle info button click here
                infoDataInitialize()

                popupRecyclerView = popupView.findViewById(R.id.pop_up_info_recyclerview)
                popupRecyclerView.layoutManager = LinearLayoutManager(context)
                infoDataAdapter = InfoDataAdapter(infoDataList)
                popupRecyclerView.adapter = infoDataAdapter

                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

                return true
            }

            R.id.Refresh ->{
                FragmentUtils().refreshFragment(requireActivity(), Sensor_data_fragment())
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun infoDataInitialize() {
        infoDataList = InfoDataInitializer.initializeInfoData(requireContext(),"Sensors")
    }

    private suspend fun sensorDataInitialize() : Boolean
    {
        attributeNames = ArrayList()
        readings = ArrayList()
        sensorDataList = ArrayList()


            val sensorDataAPI: SensorDataAPI? = APIServices.fetchSensorDataFromJson()

            if (sensorDataAPI != null) {
                val sensorDataString = sensorDataAPI.toString() // Convert SensorDataAPI to string


                val keyValuePairs = sensorDataString.substringAfter("(").substringBefore(")").split(", ")
                for (keyValuePair in keyValuePairs) {
                    val parts = keyValuePair.split("=")
                    if (parts.size == 2) {
                        val attributeName = parts[0]
                        val reading = parts[1]
                        attributeNames.add(attributeName)
                        readings.add(reading)
                    }
                }

                //hardcoded values
                sensorDataList = arrayListOf<SensorData>()

                //optimal range still use
                sensorOptimalRange = arrayOf(
                    getString(R.string.range_1),
                    getString(R.string.range_2),
                    getString(R.string.range_3),
                    getString(R.string.range_4),
                    getString(R.string.range_5),
                    getString(R.string.range_6),
                )
                sensorStatus = arrayOf(
                    getString(R.string.status_1),
                    getString(R.string.status_2),
                    getString(R.string.status_3),
                    getString(R.string.status_4),
                    getString(R.string.status_5),
                    getString(R.string.status_6)
                )

                for(i in attributeNames.indices){
                    val sensorData = SensorData(attributeNames[i], sensorOptimalRange[i],readings[i],sensorStatus[i].toBoolean())
                    sensorData.status = sensorData.isReadingWithinOptimalRange().toBoolean()
                    sensorDataList.add(sensorData)
                }

                Log.i("testing API", "Attribute Names: $attributeNames")
                Log.i("testing API", "Readings: $readings")
                return true // Data loaded successfully
            } else {
                Log.i("testing API", "Failed to Fetch sensor data: $sensorDataAPI")
                return false // Data loading failed
            }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = parentFragmentManager // Use parentFragmentManager for fragments
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.addToBackStack(null) // Add the transaction to the back stack
        fragmentTransaction.commit()
    }

    // show error if equipment does not load
    private fun showError(errorMessage: String) {
        tvSensorErrorMessage.text = errorMessage
        tvSensorErrorMessage.visibility = View.VISIBLE
        sensorDataRecyclerView.visibility = View.GONE // Hide the RecyclerView
    }

    // hide if equipment shows
    private fun hideError() {
        tvSensorErrorMessage.visibility = View.GONE
        sensorDataRecyclerView.visibility = View.VISIBLE
    }

    private fun showLoading() {
        sensorLoadingBar.visibility = View.VISIBLE
        hideError() // Hide the error message
    }

    private fun hideLoading() {
        sensorLoadingBar.visibility = View.GONE
        sensorDataRecyclerView.visibility = View.VISIBLE
    }
}