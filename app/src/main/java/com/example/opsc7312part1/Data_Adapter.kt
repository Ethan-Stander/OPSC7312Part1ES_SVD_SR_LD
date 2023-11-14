package com.example.opsc7312part1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Data_Adapter(private val dataList :ArrayList<SensorDataAPISqlLite>, context : Context): RecyclerView.Adapter<Data_Adapter.ViewHolder>() {

    val context = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Data_Adapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.data_item,parent,false)
        return Data_Adapter.ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sensorData = dataList[position]

        holder.tvDataFarmName.text = "${sensorData.farmName}"
       holder.tvTimeCalled.text = " ・ ${sensorData.timeCalled}"
        holder.tvTemperature.text = " ・ ${sensorData.Temperature}"
        holder.tvHumidity.text = " ・ ${sensorData.Humidity}"
        holder.tvLightLevel.text = " ・ ${sensorData.LightLevel}"
        holder.tvFlowRate.text = " ・ ${sensorData.FlowRate}"
        holder.tvpH.text = " ・ ${sensorData.pH}"
        holder.tvEC.text = " ・ ${sensorData.EC}"

    }

    override fun getItemCount(): Int {
        return dataList.count()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val tvTimeCalled: TextView = itemView.findViewById(R.id.tvTimeCalled)
        val tvTemperature: TextView = itemView.findViewById(R.id.tvTemperature)
        val tvHumidity: TextView = itemView.findViewById(R.id.tvHumidity)
        val tvLightLevel: TextView = itemView.findViewById(R.id.tvLightLevel)
        val tvFlowRate: TextView = itemView.findViewById(R.id.tvFlowRate)
        val tvpH: TextView = itemView.findViewById(R.id.tvpH)
        val tvEC: TextView = itemView.findViewById(R.id.tvEC)
        val tvDataFarmName: TextView = itemView.findViewById(R.id.tvDataFarmName)

    }

}