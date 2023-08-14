package com.example.opsc7312part1

import android.icu.text.IDNA.Info
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class InfoDataAdapter (private val infoDataList :ArrayList<InfoData>) : RecyclerView.Adapter<InfoDataAdapter.ViewHolder>(){

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val infoTitle : TextView = itemView.findViewById(R.id.infoTitle)
        val infoDesc : TextView = itemView.findViewById(R.id.infoDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.info_pop_ups_list_item,parent,false)
        return InfoDataAdapter.ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return  infoDataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = infoDataList[position]
        holder.infoTitle.text = currentItem.InfoTitle
        holder.infoDesc.text = currentItem.InfoDescription
    }
}