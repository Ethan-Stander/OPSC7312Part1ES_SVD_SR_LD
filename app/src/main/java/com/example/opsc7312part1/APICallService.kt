package com.example.opsc7312part1

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask

class APICallService : Service() {

    private val timer = Timer()
    private  val apiCallInterval : Long = 10 * 1000
    private val notificationServices = NotificationServices()

    override fun onCreate() {
        super.onCreate()
        notificationServices.createNotificationChannel(this)
    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        timer.scheduleAtFixedRate(object :TimerTask(){
            override fun run() {
                CoroutineScope(Dispatchers.IO).launch {
                    val sensorData = APIServices.fetchSensorDataFromJson()
                    val hardwareData = APIServices.fetchhardware()

                    notificationServices.scheduleNotification(
                        applicationContext,
                        "Condition Alert",
                        "Condition has changed to bad")
                    Log.i("Check bg service", "is it working?")
                }
            }
        },0,apiCallInterval)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }
}