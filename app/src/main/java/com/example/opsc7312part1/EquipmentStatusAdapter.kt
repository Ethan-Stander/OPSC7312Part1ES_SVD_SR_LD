package com.example.opsc7312part1

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EquipmentStatusAdapter(private val equipmentStatusDataList :ArrayList<EquipmentStatusData>, context : Context): RecyclerView.Adapter<EquipmentStatusAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener
    val context = context

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    class ViewHolder(itemView: View, listener: onItemClickListener):RecyclerView.ViewHolder(itemView) {
        val equipmentTitle : TextView = itemView.findViewById(R.id.equipmentTitle)
        val equipmentStatusSwitch : Switch = itemView.findViewById(R.id.equipmentStatusSwitch)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipmentStatusAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.equipment_status_item_view,parent,false)
        return EquipmentStatusAdapter.ViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: EquipmentStatusAdapter.ViewHolder, position: Int) {
        val currentItem = equipmentStatusDataList[position]
        holder.equipmentTitle.text = currentItem.equipmentTitle

        val context = holder.itemView.context
        // Set background color based on status
        if (currentItem.equipmentStatus) {
            // Set background and thumb/track colors to green if status is true
            holder.equipmentStatusSwitch.isChecked = true
            holder.equipmentStatusSwitch.text = "On"
            holder.equipmentStatusSwitch.thumbTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green))
            holder.equipmentStatusSwitch.trackTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green))
        } else {
            // Set background and thumb/track colors to red if status is false
            holder.equipmentStatusSwitch.isChecked = false
            holder.equipmentStatusSwitch.text = "Off"
            holder.equipmentStatusSwitch.thumbTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red))
            holder.equipmentStatusSwitch.trackTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red))
        }

        // Set switch change listener to handle changes in status
        holder.equipmentStatusSwitch.setOnCheckedChangeListener { _, isChecked ->
            // Update the status in your data model (currentItem) or perform any other necessary action
            currentItem.equipmentStatus = isChecked

            val scope = CoroutineScope(Dispatchers.Main)
            val job = scope.launch {
                var temp: Boolean = APIServices.switch(currentItem.link)
                val myDB = DatabaseHelper(context)
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val currentDateTime: String = sdf.format(Date())
                val status = !currentItem.equipmentStatus
                var action = Action(currentDateTime,currentItem.equipmentTitle,status.toString(),currentItem.equipmentStatus.toString(),false, "")
                myDB.addAction(action)

            // Update the background and thumb/track colors based on the new status
            if (isChecked) {
                holder.equipmentStatusSwitch.text = "On"
                holder.equipmentStatusSwitch.thumbTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green))
                holder.equipmentStatusSwitch.trackTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green))
            } else {
                holder.equipmentStatusSwitch.text = "Off"
                holder.equipmentStatusSwitch.thumbTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red))
                holder.equipmentStatusSwitch.trackTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.red))
            }
            }
        }

    }

    override fun getItemCount(): Int {
        return equipmentStatusDataList.size
    }
}