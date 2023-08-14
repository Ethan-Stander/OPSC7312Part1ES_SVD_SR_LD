package com.example.opsc7312part1

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView


class Equipment_status_fragment : Fragment() {


    private lateinit var equipmentStatusDataAdapter: EquipmentStatusAdapter
    private lateinit var equipmentStatusDataRecyclerView : RecyclerView
    private lateinit var equipmentStatusDataList:  ArrayList<EquipmentStatusData>

    lateinit var equipmentTitle : Array<String>
    lateinit var equipmentStatus : Array<String>

    //for pop up
    private lateinit var infoDataAdapter: InfoDataAdapter
    private lateinit var popupRecyclerView : RecyclerView
    private lateinit var infoDataList:  ArrayList<InfoData>

    lateinit var infoTitle : Array<String>
    lateinit var infoDesc : Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //info pop up
        val popupButton :ImageButton = view.findViewById(R.id.popupButton)
        val backgroundOverlay: View = view.findViewById(R.id.bgOverlay)
        val popupView = LayoutInflater.from(context).inflate(R.layout.info_pop_ups,null)
        val popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true)

        popupButton.setOnClickListener {

            infoDataInitialize()
            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
            backgroundOverlay.visibility = View.VISIBLE

            popupRecyclerView = popupView.findViewById(R.id.pop_up_info_recyclerview)
            popupRecyclerView.layoutManager = LinearLayoutManager(context)
            infoDataAdapter = InfoDataAdapter(infoDataList)
            popupRecyclerView.adapter = infoDataAdapter
        }

        //exit button on pop up
        val exitButton :ImageButton = popupView.findViewById(R.id.exitButton)
        exitButton.setOnClickListener {
            backgroundOverlay.visibility = View.GONE
            popupWindow.dismiss()

        }

        equipmentDataInitialize()
        val gridLayoutManager = GridLayoutManager(context,2)
        equipmentStatusDataRecyclerView = view.findViewById(R.id.EquipmentStatusRecyclerView)
        equipmentStatusDataRecyclerView.layoutManager = gridLayoutManager
        equipmentStatusDataRecyclerView.setHasFixedSize(true)
        equipmentStatusDataAdapter = EquipmentStatusAdapter(equipmentStatusDataList)
        equipmentStatusDataRecyclerView.adapter = equipmentStatusDataAdapter

        //Redirect to adjustment fragment
        equipmentStatusDataAdapter.setOnItemClickListener(object :EquipmentStatusAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
            }

        })

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.overlay_layout, container, false)
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
        infoDataList = arrayListOf<InfoData>()

        infoTitle = arrayOf(
            getString(R.string.equip_title_1),
            getString(R.string.equip_title_2),
            getString(R.string.equip_title_3),
            getString(R.string.equip_title_4),
            getString(R.string.equip_title_5),
             getString(R.string.equip_title_6)
        )

        infoDesc = arrayOf(
            getString(R.string.equip_desc_1),
            getString(R.string.equip_desc_2),
            getString(R.string.equip_desc_3),
            getString(R.string.equip_desc_4),
            getString(R.string.equip_desc_5),
            getString(R.string.equip_desc_6)
        )


        for(i in infoTitle.indices){
            val infoData = InfoData(infoTitle[i], infoDesc[i])
            infoDataList.add(infoData)
        }
    }

    private fun equipmentDataInitialize()
    {
        //hardcoded values
        equipmentStatusDataList = arrayListOf<EquipmentStatusData>()

        equipmentTitle = arrayOf(
            getString(R.string.equipment_1),
            getString(R.string.equipment_2),
            getString(R.string.equipment_3),
            getString(R.string.equipment_4),
            getString(R.string.equipment_5)
        )

        equipmentStatus = arrayOf(
            getString(R.string.equipment_status_1),
            getString(R.string.equipment_status_2),
            getString(R.string.equipment_status_3),
            getString(R.string.equipment_status_4),
            getString(R.string.equipment_status_5),
        )


        for(i in equipmentTitle.indices){
            val equipmentStatusData = EquipmentStatusData(equipmentTitle[i], equipmentStatus[i].toBoolean())
            equipmentStatusDataList.add(equipmentStatusData)
        }


    }
}