package com.example.opsc7312part1

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.cardview.widget.CardView
import androidx.compose.ui.layout.Layout
import androidx.recyclerview.widget.RecyclerView


class SensorDataAdapter(private val sensorDataList: ArrayList<SensorData>): RecyclerView.Adapter<SensorDataAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    class ViewHolder(itemView: View, listener: onItemClickListener):RecyclerView.ViewHolder(itemView)
    {
        val sensorTitle : TextView = itemView.findViewById(R.id.textViewTitle)
        val optimalRangeData : TextView = itemView.findViewById(R.id.textViewOptimalRangeData)
        val currentReadingData : TextView = itemView.findViewById(R.id.textViewCurrentReadingData)
        val bgColor : CardView = itemView.findViewById(R.id.sensorDataItemView)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.sensor_data_list_item,parent,false)
        return  ViewHolder(itemView,mListener)
    }

    override fun getItemCount(): Int {
        return sensorDataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = sensorDataList[position]
        holder.sensorTitle.text = currentItem.sensorTitle
        holder.optimalRangeData.text = currentItem.sensorOptimalRange
        holder.currentReadingData.text = currentItem.sensorCurrentReading

        val context = holder.itemView.context
        // Set background color based on status
        if (currentItem.status) {
            // Set background to green if status is true
            /*holder.color.setCardBackgroundColor(Color.parseColor(currentItem.Color.toString()))*/
            holder.bgColor.setCardBackgroundColor(context.getColor(R.color.green))
        } else {
            // Set background to red if status is false
            holder.bgColor.setCardBackgroundColor(context.getColor(R.color.red))
        }

    }
}