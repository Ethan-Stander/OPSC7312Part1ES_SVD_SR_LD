package com.example.opsc7312part1

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat


const val notificationID = 1
const val chanelID = "Channel1"
const val notificationTitle = "title"
const val notificationMessage = "message"
const val NOTIFY_USER = "com.example.opsc7312part1.NOTIFY_USER"
class NotificationServices :BroadcastReceiver(){

    @SuppressLint("NotificationPermission")
    override fun onReceive(context: Context, intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notification : Notification = NotificationCompat.Builder(context, chanelID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle(intent.getStringExtra(notificationTitle))
                .setContentText(intent.getStringExtra(notificationMessage))
                .build()

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(notificationID, notification)
        }
    }

    @SuppressLint("NewApi")
    fun createNotificationChannel(context: Context) {
        //checks to see if device support notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification channel"
            val desc = "testing notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(chanelID, name, importance)

            channel.description = desc

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

        }
    }

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleNotification(
        context: Context,
        notificationTitle: String,
        notificationMessage: String,
    ) {
        val intent = Intent(context, NotificationServices::class.java)
        intent.putExtra(notificationTitle, notificationTitle)
        intent.putExtra(notificationMessage, notificationMessage)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            pendingIntent
        )
    }

}