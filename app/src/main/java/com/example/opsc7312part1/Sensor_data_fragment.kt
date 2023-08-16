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
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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


        val scope = CoroutineScope(Dispatchers.Main)
        val job = scope.launch {
            sensorDataInitialize()
            val layoutManager = LinearLayoutManager(context)
            sensorDataRecyclerView = view.findViewById(R.id.SensorDataRecyclerView)
            sensorDataRecyclerView.layoutManager = layoutManager
            sensorDataRecyclerView.setHasFixedSize(true)
            sensorDataAdapter = SensorDataAdapter(sensorDataList)
            sensorDataRecyclerView.adapter = sensorDataAdapter

            // Redirect to adjustment fragment
            sensorDataAdapter.setOnItemClickListener(object : SensorDataAdapter.onItemClickListener {
                override fun onItemClick(position: Int) {
                    val sensorTitle = sensorTitle[position]
                    val sensorAdjFragment = Sensor_adjustment_fragment()
                    replaceFragment(sensorAdjFragment)
                    (activity as AppCompatActivity).title = "$sensorTitle : Sensor Adjustment"
                }
            })
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
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun infoDataInitialize() {
        infoDataList = InfoDataInitializer.initializeInfoData(requireContext(),"Sensors")
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Equipment_data_fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Sensor_data_fragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
    private suspend fun sensorDataInitialize()
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

                sensorTitle = arrayOf(
                    getString(R.string.head_1),
                    getString(R.string.head_2),
                    getString(R.string.head_3),
                    getString(R.string.head_4),
                )

                //optimal range still use
                sensorOptimalRange = arrayOf(
                    getString(R.string.range_1),
                    getString(R.string.range_2),
                    getString(R.string.range_3),
                    getString(R.string.range_4),
                    getString(R.string.range_5),
                    getString(R.string.range_6),
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
                    getString(R.string.status_5),
                    getString(R.string.status_6)
                )

                for(i in attributeNames.indices){
                    val sensorData = SensorData(attributeNames[i], sensorOptimalRange[i],readings[i],sensorStatus[i].toBoolean())
                    sensorDataList.add(sensorData)
                }

                Log.i("testing API", "Attribute Names: $attributeNames")
                Log.i("testing API", "Readings: $readings")
            } else {
                Log.i("testing API", "Failed to Fetch sensor data: $sensorDataAPI")
            }
        }



    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = parentFragmentManager // Use parentFragmentManager for fragments
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.addToBackStack(null) // Add the transaction to the back stack
        fragmentTransaction.commit()
    }



}