package com.example.opsc7312part1

import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
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
import android.widget.Switch
import android.widget.TextView
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class Equipment_status_fragment : Fragment() {

    private lateinit var equipmentStatusDataAdapter: EquipmentStatusAdapter
    private lateinit var equipmentStatusDataRecyclerView: RecyclerView
    private lateinit var equipmentStatusDataList: ArrayList<EquipmentStatusData>

    lateinit var equipmentTitle: Array<String>
    lateinit var equipmentStatus: Array<String>
    lateinit var Links: Array<String>

    //for pop up info
    private lateinit var infoDataAdapter: InfoDataAdapter
    private lateinit var popupRecyclerView: RecyclerView
    private lateinit var infoDataList: ArrayList<InfoData>

    private lateinit var popupWindow: PopupWindow
    private lateinit var popupView: View

    private lateinit var tvErrorMessage: TextView

    private lateinit var equipmentLoadingBar: ProgressBar

    //AI control/predictions
    private lateinit var tvHeader: TextView
    private lateinit var AISwicth: Switch
    private lateinit var tvPredictionsHeader: TextView
    private lateinit var tvPrediction: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //AI control/predictions
        tvHeader = view.findViewById(R.id.tvHeader)
        AISwicth = view.findViewById(R.id.AIStatusSwitch)
        tvPredictionsHeader = view.findViewById(R.id.tvPredictionsHeader)
        tvPrediction = view.findViewById(R.id.tvPrediction)

        equipmentStatusDataRecyclerView = view.findViewById(R.id.EquipmentStatusRecyclerView)

        equipmentLoadingBar = view.findViewById(R.id.equipmentLoadingBar)

        tvErrorMessage = view.findViewById(R.id.tvErrorMessage)

        //info pop up
        popupView = LayoutInflater.from(context).inflate(R.layout.info_pop_ups, null)
        popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            true
        )

        //exit button on pop up
        val exitButton: ImageButton = popupView.findViewById(R.id.exitButton)
        exitButton.setOnClickListener {
            popupWindow.dismiss()
        }

        //calls AI control and predictions to check if AI is on/off
        val scope = CoroutineScope(Dispatchers.Main)
        val job = scope.launch {
            showLoading()
            predictionsDataInitialize()
            val predictionTemp = APIServices.fetchPredictions()
            if (predictionTemp != null) {
                if (predictionTemp.AI_Status == "1") {
                    AISwicth.text = "On"
                    AISwicth.thumbTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireActivity(), R.color.green
                        )
                    )
                    AISwicth.trackTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireActivity(), R.color.green
                        )
                    )
                    showPredictions()
                    tvPrediction.text = "${predictionTemp.Temperature}\n${predictionTemp.Humidity}\n${predictionTemp.pH}\n${predictionTemp.EC}\n"
                    hideEquipmentControls()

                } else {
                    //disables AI predictions display
                    AISwicth.text = "Off"
                    AISwicth.thumbTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireActivity(), R.color.red
                        )
                    )
                    AISwicth.trackTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(
                            requireActivity(), R.color.red
                        )
                    )
                    hidePredictions()

                    //Enables equipment controls display
                    equipmentDataInitialize()
                    val gridLayoutManager = GridLayoutManager(context, 2)
                    equipmentStatusDataRecyclerView = view.findViewById(R.id.EquipmentStatusRecyclerView)
                    equipmentStatusDataRecyclerView.layoutManager = gridLayoutManager
                    equipmentStatusDataRecyclerView.setHasFixedSize(true)
                    equipmentStatusDataAdapter = EquipmentStatusAdapter(equipmentStatusDataList,requireContext())
                    equipmentStatusDataRecyclerView.adapter = equipmentStatusDataAdapter

                    //switch for API calls
                    equipmentStatusDataAdapter.setOnItemClickListener(object :
                        EquipmentStatusAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                        }
                    })
                }
            }
            hideLoading()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.equipment_status_fragment, container, false)
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

    private suspend fun predictionsDataInitialize() {
        val predictions = APIServices.fetchPredictions()

        if (predictions != null) {
            if (predictions.AI_Status == "1") {
                AISwicth.isChecked = true
                showPredictions()
                hideEquipmentControls()
                tvPrediction.text =
                    "${predictions.Temperature}\n${predictions.Humidity}\n${predictions.pH}\n${predictions.EC}\n"

            }else
            {
                AISwicth.isChecked = false
                hidePredictions()
                showEquipmentControls()
           }
            hideLoading()
            hideError()
        } else {
            hideLoading()
            showError("Error loading controls...\n (please reconnect)")
        }

        AISwicth.setOnCheckedChangeListener { _, isChecked ->
            val scope = CoroutineScope(Dispatchers.Main)
            val job = scope.launch {

                var temp: Boolean = APIServices.toggle_AI_Switch()


                Log.i("toggle AI switch", "THE API CALL WAS HIT: $temp")

                if (temp != null) {
                    if (!temp) {
                        Log.i("toggle AI switch", "Call was null and invalid")

                    } else {
                        Log.i("toggle AI switch", "API call was valid")
                    }
                }

                val predictionstemp = APIServices.fetchPredictions()
                if (predictionstemp != null) {
                    if (predictionstemp.AI_Status == "1") {
                        AISwicth.text = "On"
                        AISwicth.thumbTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(
                                requireActivity(), R.color.green
                            )
                        )
                        AISwicth.trackTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(
                                requireActivity(), R.color.green
                            )
                        )
                        showPredictions()
                        hideEquipmentControls()

                        if (predictions != null) {
                            tvPrediction.text =
                                "${predictionstemp.Temperature}\n${predictionstemp.Humidity}\n${predictionstemp.pH}\n${predictionstemp.EC}\n"
                        }

                    } else {
                        AISwicth.text = "Off"
                        AISwicth.thumbTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(
                                requireActivity(), R.color.red
                            )
                        )
                        AISwicth.trackTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(
                                requireActivity(), R.color.red
                            )
                        )
                        hidePredictions()
                        showEquipmentControls()



                        equipmentDataInitialize()
                        equipmentStatusDataAdapter = EquipmentStatusAdapter(equipmentStatusDataList,requireContext())
                        equipmentStatusDataRecyclerView.adapter = equipmentStatusDataAdapter
                        //switch for API calls
                        equipmentStatusDataAdapter.setOnItemClickListener(object :
                            EquipmentStatusAdapter.onItemClickListener {
                            override fun onItemClick(position: Int) {
                            }
                        })


                    }
                }
            }
        }
    }

    private fun showEquipmentControls() {
        equipmentStatusDataRecyclerView.visibility = View.VISIBLE
    }

    private fun hidePredictions() {
        tvPrediction.visibility = View.GONE
        tvPredictionsHeader.visibility = View.GONE

    }

    private fun hideEquipmentControls() {
        equipmentStatusDataRecyclerView.visibility = View.GONE
    }

    private fun showPredictions() {
        tvPrediction.visibility = View.VISIBLE
        tvPredictionsHeader.visibility = View.VISIBLE

    }

    private fun infoDataInitialize() {
        //hardcoded values for equipment info pop up
        infoDataList = InfoDataInitializer.initializeInfoData(requireContext(), "Equipment")
    }

    private suspend fun equipmentDataInitialize() {

        var Statuses: hardware? = context?.let { APIServices.fetchhardware(it) }

        if (Statuses != null) {
            Statuses.setValues()
            equipmentStatus = Statuses.getAllStatuses()
        } else {
            equipmentStatus = emptyArray()
        }

        // check / show error
        if (equipmentStatus.isNullOrEmpty()) {
            showError("Error loading controls...\n (please reconnect)")
        } else {
            hideError()
        }

        Log.d("equipmentStatus", equipmentStatus.joinToString(", "))

        //hardcoded values
        equipmentStatusDataList = arrayListOf<EquipmentStatusData>()

        equipmentTitle = hardware.attributeNames
        Links = hardware.links

        for (i in equipmentStatus.indices) {
            val equipmentStatusData =
                EquipmentStatusData(equipmentTitle[i], equipmentStatus[i].toBoolean(), Links[i])
            equipmentStatusDataList.add(equipmentStatusData)
        }
    }

    // show error if equipment does not load
    private fun showError(errorMessage: String) {
        tvErrorMessage.text = errorMessage
        tvErrorMessage.visibility = View.VISIBLE
        equipmentStatusDataRecyclerView.visibility = View.GONE
    }

    // hide if equipment shows
    private fun hideError() {
        tvErrorMessage.visibility = View.GONE
        equipmentStatusDataRecyclerView.visibility = View.VISIBLE
    }

    private fun showLoading() {
        equipmentLoadingBar.visibility = View.VISIBLE
        hideError() // Hide the error message
    }

    private fun hideLoading() {
        equipmentLoadingBar.visibility = View.GONE
        equipmentStatusDataRecyclerView.visibility = View.VISIBLE
    }

}