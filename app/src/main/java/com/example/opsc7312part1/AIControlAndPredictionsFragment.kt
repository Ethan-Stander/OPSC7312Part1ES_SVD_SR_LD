package com.example.opsc7312part1

import android.content.res.ColorStateList
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
import android.widget.PopupWindow
import android.widget.ProgressBar
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.http2.Header


class AIControlAndPredictionsFragment : Fragment() {
    //for pop up info
    private lateinit var infoDataAdapter: InfoDataAdapter
    private lateinit var popupRecyclerView: RecyclerView
    private lateinit var infoDataList :ArrayList<InfoData>
    private lateinit var popupWindow: PopupWindow
    private lateinit var popupView: View

    //loading bar/error message
    private lateinit var tvPredictionsErrorMessage: TextView
    private lateinit var predictionLoadingBar: ProgressBar

    //AI control/predictions
    private lateinit var tvHeader: TextView
    private lateinit var AISwicth : Switch
    private lateinit var tvPredictionsHeader : TextView
    private lateinit var tvPrediction : TextView

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


        //info pop up
        popupView = LayoutInflater.from(context).inflate(R.layout.info_pop_ups,null)
        popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true)

        //exit button on pop up
        val exitButton : ImageButton = popupView.findViewById(R.id.exitButton)
        exitButton.setOnClickListener {
            popupWindow.dismiss()
        }

        //add the UI components
        predictionLoadingBar = view.findViewById(R.id.predictionLoadingBar)
        tvPredictionsErrorMessage = view.findViewById(R.id.tvPredictionErrorMessage)


        //display predictions
        val scope = CoroutineScope(Dispatchers.Main)
        val job = scope.launch {
            showLoading()
            predictionsDataInitialize()
            val predictionstemp = APIServices.fetchPredictions()
            if (predictionstemp != null) {
                if (predictionstemp.AI_Status == "1") {
                    AISwicth.text = "On"
                    AISwicth.thumbTintList = ColorStateList.valueOf(ContextCompat.getColor(requireActivity(), R.color.green))
                    AISwicth.trackTintList = ColorStateList.valueOf(ContextCompat.getColor(requireActivity(), R.color.green))
                }else {
                    AISwicth.text = "Off"
                    AISwicth.thumbTintList = ColorStateList.valueOf(ContextCompat.getColor(requireActivity(), R.color.red))
                    AISwicth.trackTintList = ColorStateList.valueOf(ContextCompat.getColor(requireActivity(), R.color.red))
                }
            }
        }

    }

    private suspend fun predictionsDataInitialize() {
        val predictions = APIServices.fetchPredictions()

        var AICurrentStatus = false

        if (predictions != null) {
            if (predictions.AI_Status == "1") {
                AICurrentStatus = true
            }
                hideLoading()
                hideError()
                showPredictions()
        }else
        {
            hideLoading()
            showError("Error loading predictions...\n (please reconnect)")
        }


            AISwicth.setOnCheckedChangeListener { _, isChecked ->
                AICurrentStatus = isChecked


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
                            AISwicth.thumbTintList = ColorStateList.valueOf(ContextCompat.getColor(requireActivity(), R.color.green))
                            AISwicth.trackTintList = ColorStateList.valueOf(ContextCompat.getColor(requireActivity(), R.color.green))
                        }else {
                            AISwicth.text = "Off"
                            AISwicth.thumbTintList = ColorStateList.valueOf(ContextCompat.getColor(requireActivity(), R.color.red))
                            AISwicth.trackTintList = ColorStateList.valueOf(ContextCompat.getColor(requireActivity(), R.color.red))
                        }
                    }
                }
            }

            if (predictions != null) {
                tvPrediction.text =
                    "${predictions.Temperature}\n${predictions.Humidity}\n${predictions.pH}\n${predictions.EC}\n"
            }

        }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootview = inflater.inflate(R.layout.fragment_ai_control_and_predictions, container, false)
        (activity as AppCompatActivity).supportActionBar?.apply {
            setHasOptionsMenu(true)
        }
        return rootview
    }

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
        infoDataList = InfoDataInitializer.initializeInfoData(requireContext(),"Control/Predictions")
    }

    private fun showError(errorMessage: String) {
        tvPredictionsErrorMessage.text = errorMessage
        tvPredictionsErrorMessage.visibility = View.VISIBLE
        predictionLoadingBar.visibility = View.GONE

        tvHeader.visibility = View.GONE
        tvPrediction.visibility = View.GONE
        tvPredictionsHeader.visibility = View.GONE
        AISwicth.visibility = View.GONE
    }

    // hide if predictions shows
    private fun hideError() {
        tvPredictionsErrorMessage.visibility = View.GONE
    }

    private fun showLoading() {
        predictionLoadingBar.visibility = View.VISIBLE
        hideError()
    }

    private fun hideLoading() {
        predictionLoadingBar.visibility = View.GONE
    }

    private fun showPredictions() {
        tvHeader.visibility = View.VISIBLE
        tvPrediction.visibility = View.VISIBLE
        tvPredictionsHeader.visibility = View.VISIBLE
        AISwicth.visibility = View.VISIBLE
    }
}