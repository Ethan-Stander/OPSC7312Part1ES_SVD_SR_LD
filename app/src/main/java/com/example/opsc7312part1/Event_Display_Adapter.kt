package com.example.opsc7312part1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventAdapter(private val eventList :ArrayList<Action>, context : Context): RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    val context = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.event_item,parent,false)
        return EventAdapter.ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val action = eventList[position]
        // Set the data to your TextViews or other UI elements in the ViewHolder
        holder.tvDate.text = "Date: ${action.Date}"
        holder.tvEquipmentChanged.text = "Equipment Changed: ${action.EquipmentChanged}"
        holder.tvPreviousState.text = "Previous State: ${action.PreviousState}"
        holder.tvNewState.text = "New State: ${action.NewState}"
        holder.tvIsDeleted.text = "Deleted: ${action.IsDeleted}"
        holder.tvFarmName.text = "Deleted: ${action.farmName}"
    }

    override fun getItemCount(): Int {
        return eventList.count()
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvEquipmentChanged: TextView = itemView.findViewById(R.id.tvEquipmentChanged)
        val tvPreviousState: TextView = itemView.findViewById(R.id.tvPreviousState)
        val tvNewState: TextView = itemView.findViewById(R.id.tvNewState)
        val tvIsDeleted: TextView = itemView.findViewById(R.id.tvIsDeleted)
        val tvFarmName: TextView = itemView.findViewById(R.id.tvFarmName)

    }

}