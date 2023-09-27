package com.example.opsc7312part1

import android.content.Context
import android.icu.text.IDNA.Info
import java.security.AccessControlContext

//class to get hardcoded values from string.xml to be called in separate fragments tp populate the info adapter
//object defines a single instance of the class
object InfoDataInitializer {
    fun initializeInfoData(context: Context,infoTitle:String):ArrayList<InfoData>{
        val infoDataList = arrayListOf<InfoData>()

        var infoTitles: Array<String>
        var infoDescriptions: Array<String>

        when (infoTitle) {
            "Equipment" -> {
                infoTitles = context.resources.getStringArray(R.array.equip_info_title)
                infoDescriptions = context.resources.getStringArray(R.array.equip_info_desc)
            }
            "Sensors" -> {
                infoTitles = context.resources.getStringArray(R.array.sensor_info_title)
                infoDescriptions = context.resources.getStringArray(R.array.sensor_info_desc)
            }
            "Control/Predictions" ->{
                infoTitles = context.resources.getStringArray(R.array.control_prediction_info_title)
                infoDescriptions = context.resources.getStringArray(R.array.control_prediction_info_desc)
            }
            else -> {
                infoTitles = emptyArray() 
                infoDescriptions = emptyArray()
            }
        }

        for (i in infoTitles.indices)
        {
            val infoData = InfoData(infoTitles[i],infoDescriptions[i])
            infoDataList.add(infoData)
        }

        return infoDataList
    }
}