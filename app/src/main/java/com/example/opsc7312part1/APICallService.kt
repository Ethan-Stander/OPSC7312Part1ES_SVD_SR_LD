package com.example.opsc7312part1

import android.Manifest
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask

class APICallService : Service() {

    private val timer = Timer()
    private val apiCallInterval: Long = 15 * 1000 // 15 seconds


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            Actions.START.toString() -> start()
            Actions.STOP.toString() -> stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start(){

        timer.scheduleAtFixedRate(object :TimerTask(){
            override fun run() {
                CoroutineScope(Dispatchers.IO).launch {
                    val sensorData = APIServices.fetchSensorDataFromJson()
                    val hardwareData = APIServices.fetchhardware()

                    if (hardwareData != null) {
                        hardwareData.setValues()
                        if (hardwareData.getAllStatuses().contains("1")) {
                            createNotification("Equipment Warning","ERROR: EQUIPMENT OFFLINE" )

                            Log.i("Check bg service", "hardware not on")
                        }
                    } else
                        if (hardwareData == null) {
                            createNotification("hardware data is empty",
                                "hardware data is empty ")
                            Log.i("Check bg service", "hardware not found")
                        }
                }
            }
        },0,apiCallInterval)
        val notification = createNotification("SmartHydro", "Real time updates")
        startForeground(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

    enum class Actions{
        START,STOP
    }
    private fun createNotification(title: String, message: String): Notification {
        val intent = Intent(applicationContext, GoogleLogin::class.java)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )



        val customNotificationLayout = RemoteViews(applicationContext.packageName, R.layout.warning_notification_layout)
        customNotificationLayout.setTextViewText(R.id.txtNotificationHeader, "SmartHydro")
        customNotificationLayout.setTextViewText(R.id.txtNotificationTitle, title)
        customNotificationLayout.setTextViewText(R.id.txtNotificationDescription, message)

        val notification = NotificationCompat.Builder(applicationContext, "Channel_id")
            .setContentIntent(pendingIntent)
            .setCustomContentView(customNotificationLayout)
            .setSmallIcon(R.drawable.sh_logo)
            .build()

        val notificationManager = NotificationManagerCompat.from(applicationContext)
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            notificationManager.notify(1, notification)
        }
        return notification
    }


}




