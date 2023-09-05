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
    private val apiCallInterval: Long = 10 * 1000

    override fun onCreate() {
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {


                CoroutineScope(Dispatchers.IO).launch {
                    val sensorData = APIServices.fetchSensorDataFromJson()
                    val hardwareData = APIServices.fetchhardware()

                    if (hardwareData != null) {
                        hardwareData.setValues()
                        if (hardwareData.getAllStatuses().contains("1")) {
                            showNotification("Equipment Warning","ERROR: EQUIPMENT OFFLINE" )

                            Log.i("Check bg service", "hardware not on")
                        }

                    } else
                        if (hardwareData == null) {
                            showNotification("hardware data is empty",
                                    "hardware data is empty ")
                            Log.i("Check bg service", "hardware not found")
                        }
                }
            }
        }, 0, apiCallInterval)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

    private fun showNotification(title: String, message: String)
    {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(applicationContext, "Channel_id")
                .setContentText(title)
                .setContentTitle(message)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .build()
        notificationManager.notify(1,notification)
    }
}




