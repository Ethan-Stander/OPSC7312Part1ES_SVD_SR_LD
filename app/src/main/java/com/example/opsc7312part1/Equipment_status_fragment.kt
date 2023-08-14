package com.example.opsc7312part1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class Equipment_status_fragment : Fragment() {


    private lateinit var equipmentStatusDataAdapter: EquipmentStatusAdapter
    private lateinit var equipmentStatusDataRecyclerView : RecyclerView
    private lateinit var equipmentStatusDataList:  ArrayList<EquipmentStatusData>

    lateinit var equipmentTitle : Array<String>
    lateinit var equipmentStatus : Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        return inflater.inflate(R.layout.equipment_status_fragment, container, false)
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