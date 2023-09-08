package com.example.opsc7312part1

import android.os.Bundle
import android.os.Handler
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class Equipment_status_fragment : Fragment() {


    private lateinit var equipmentStatusDataAdapter: EquipmentStatusAdapter
    private lateinit var equipmentStatusDataRecyclerView : RecyclerView
    private lateinit var equipmentStatusDataList:  ArrayList<EquipmentStatusData>

    lateinit var equipmentTitle : Array<String>
    lateinit var equipmentStatus : Array<String>
    lateinit var Links : Array<String>

    //for pop up info
    private lateinit var infoDataAdapter: InfoDataAdapter
    private lateinit var popupRecyclerView : RecyclerView
    private lateinit var infoDataList:  ArrayList<InfoData>

    private lateinit var popupWindow: PopupWindow
    private lateinit var popupView : View

    private lateinit var tvErrorMessage: TextView

    private lateinit var equipmentLoadingBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        equipmentStatusDataRecyclerView = view.findViewById(R.id.EquipmentStatusRecyclerView)

        equipmentLoadingBar = view.findViewById(R.id.equipmentLoadingBar)

        tvErrorMessage = view.findViewById(R.id.tvErrorMessage)

        //info pop up
         popupView = LayoutInflater.from(context).inflate(R.layout.info_pop_ups,null)
         popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true)


        //exit button on pop up
        val exitButton :ImageButton = popupView.findViewById(R.id.exitButton)
        exitButton.setOnClickListener {
            popupWindow.dismiss()

        }

        //display equipment data
        val scope = CoroutineScope(Dispatchers.Main)
        val job = scope.launch {
            showLoading() // Show loading bar
            equipmentDataInitialize()
            val gridLayoutManager = GridLayoutManager(context, 2)
            equipmentStatusDataRecyclerView = view.findViewById(R.id.EquipmentStatusRecyclerView)
            equipmentStatusDataRecyclerView.layoutManager = gridLayoutManager
            equipmentStatusDataRecyclerView.setHasFixedSize(true)
            equipmentStatusDataAdapter = EquipmentStatusAdapter(equipmentStatusDataList)
            equipmentStatusDataRecyclerView.adapter = equipmentStatusDataAdapter

            //Redirect to adjustment fragment
            equipmentStatusDataAdapter.setOnItemClickListener(object :
                EquipmentStatusAdapter.onItemClickListener {
                override fun onItemClick(position: Int) {
                }

            })
            hideLoading()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

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
            Equipment_status_fragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    private fun infoDataInitialize() {
        //hardcoded values for equipment info pop up
        infoDataList = InfoDataInitializer.initializeInfoData(requireContext(),"Equipment")
    }

    private suspend fun equipmentDataInitialize()
    {

            var Statuses : hardware? = APIServices.fetchhardware()

        if (Statuses != null) {
            Statuses.setValues()
            equipmentStatus = Statuses.getAllStatuses()
        }

        else
        {
            equipmentStatus = emptyArray()
        }

        delay(1000)
        // check / show error
        if (equipmentStatus.isNullOrEmpty()) {
            showError("Error loading equipment...\n (please reconnect)")
        } else {
            hideError()
        }

            //hardcoded values
            equipmentStatusDataList = arrayListOf<EquipmentStatusData>()

            equipmentTitle = hardware.attributeNames
            Links = hardware.links

            for (i in equipmentStatus.indices) {
                val equipmentStatusData =
                    EquipmentStatusData(equipmentTitle[i], equipmentStatus[i].toBoolean(),Links[i])
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