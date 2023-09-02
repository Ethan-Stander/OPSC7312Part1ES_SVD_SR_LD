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
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


const val notificationID = 1
const val chanelID = "Channel1"
const val notificationTitle = "title"
const val notificationMessage = "message"
const val PERMISSION_REQUEST_CODE = 123
class NotificationServices :BroadcastReceiver(){

    @SuppressLint("NotificationPermission")
    override fun onReceive(context: Context, intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE) {

            val title = intent.getStringExtra(notificationTitle)
            val message = intent.getStringExtra(notificationMessage)
            val notificationTimeMillis = System.currentTimeMillis()
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            val formattedTime = sdf.format(Date(notificationTimeMillis))

            val notificationIntent = Intent(context,GoogleLogin::class.java)
            val pendingIntent = PendingIntent.getActivity(
                    context,
                    notificationID,
                    notificationIntent,
                    PendingIntent.FLAG_IMMUTABLE
            )

            val contentView = RemoteViews(context.packageName, R.layout.warning_notification_layout)
            contentView.setTextViewText(R.id.txtNotificationTitle, title)
            contentView.setTextViewText(R.id.txtNotificationDescription, message)
            contentView.setTextViewText(R.id.txtNotificationTimestamp,formattedTime.toString())

            val notification : Notification = NotificationCompat.Builder(context, chanelID)
                    .setSmallIcon(R.drawable.ic_notifications)
                    .setCustomContentView(contentView)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build()

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(notificationID, notification)
        }
    }

    @SuppressLint("NewApi")
    fun createNotificationChannel(context: Context) {
        //checks to see if device support notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE) {
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
        title: String,
        message: String,
    ) {
        val intent = Intent(context, NotificationServices::class.java)
        intent.putExtra(notificationTitle, title)
        intent.putExtra(notificationMessage, message)

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

