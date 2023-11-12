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

    }

    override fun getItemCount(): Int {
        return eventList.count()
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {


    }

}