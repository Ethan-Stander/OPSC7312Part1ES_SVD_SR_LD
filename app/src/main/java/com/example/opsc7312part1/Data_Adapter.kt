package com.example.opsc7312part1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class Data_Adapter(private val dataList :ArrayList<SensorDataAPISqlLite>, context : Context): RecyclerView.Adapter<Data_Adapter.ViewHolder>() {

    val context = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Data_Adapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.event_item,parent,false)
        return Data_Adapter.ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return dataList.count()
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {


    }

}