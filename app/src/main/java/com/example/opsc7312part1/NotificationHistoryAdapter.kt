package com.example.opsc7312part1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotificationHistoryAdapter(private val notifications: List<NotificationDataClass>) :
    RecyclerView.Adapter<NotificationHistoryAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notificationHeaderTextView: TextView = itemView.findViewById(R.id.txtNotificationHeader)
        val notificationTimestampTextView: TextView = itemView.findViewById(R.id.txtNotificationTimestamp)
        val notificationTitleTextView: TextView = itemView.findViewById(R.id.txtNotificationTitle)
        val notificationDescriptionTextView: TextView = itemView.findViewById(R.id.txtNotificationDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.warning_notification_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val notification = notifications[position]

        // Bind notification data to the views
        holder.notificationHeaderTextView.text = notification.notificationType
        holder.notificationTimestampTextView.text = notification.timestamp
        holder.notificationTitleTextView.text = notification.notificationType
        holder.notificationDescriptionTextView.text = notification.notificationMessage
    }

    override fun getItemCount(): Int {
        return notifications.size
    }
}