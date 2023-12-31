package com.example.opsc7312part1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventAdapter(private var eventList :ArrayList<Action>, context : Context): RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    val context = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.event_item,parent,false)
        return EventAdapter.ViewHolder(itemView)
    }
    fun setFilteredList(actionList: ArrayList<Action>) {
        this.eventList = actionList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val action = eventList[position]

        holder.tvDate.text = " ・ ${action.Date}"
        holder.tvEquipmentChanged.text = " ・ ${action.EquipmentChanged}"
        if (action.PreviousState == "true") {
            holder.tvPreviousState.text = " ・ On"
        }
        else if (action.PreviousState == "false") {
            holder.tvPreviousState.text = " ・ Off"
        }

        if (action.NewState == "true") {
            holder.tvNewState.text = " ・ On"
        }
        else if (action.NewState == "false") {
            holder.tvNewState.text = " ・ Off"
        }
        holder.tvFarmName.text = "${action.farmName}"
    }

    override fun getItemCount(): Int {
        return eventList.count()
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvEquipmentChanged: TextView = itemView.findViewById(R.id.tvEquipmentChanged)
        val tvPreviousState: TextView = itemView.findViewById(R.id.tvPreviousState)
        val tvNewState: TextView = itemView.findViewById(R.id.tvNewState)
        val tvFarmName: TextView = itemView.findViewById(R.id.tvFarmName)

    }

}