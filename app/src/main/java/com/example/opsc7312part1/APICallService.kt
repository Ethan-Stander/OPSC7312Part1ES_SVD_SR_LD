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
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

class APICallService : Service() {

    private val timer = Timer()
    private val apiCallInterval: Long = 10 * 1000
    private var title = ""
    private var message = ""

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

                    val hardwareData = APIServices.fetchhardware()

                    if (hardwareData != null) {
                        hardwareData.setValues()

                        when {
                            (hardwareData.Circulation_Pump_Status.equals("False")) -> {
                                title = "Equipment Warning"
                                message = "ERROR: CIRCULATION PUMP OFFLINE"
                            }

                            (hardwareData.Fan_Extractor_Status.equals("False")) -> {
                                title = "Equipment Warning"
                                message = "ERROR: EXTRACTOR FAN OFFLINE"
                            }

                            (hardwareData.Fan_Tent_Status.equals("False")) -> {
                                title = "Equipment Warning"
                                message = "ERROR: CIRCULATION FAN OFFLINE"
                            }

                            (hardwareData.Light_Status.equals("False")) -> {
                                title = "Equipment Warning"
                                message = "ERROR: LIGHT OFFLINE"
                            }

                        }
                    } else {
                        title = "System Warning"
                        message = "ERROR: EQUIPMENT NOT FOUND"
                        Log.i("Check foreground  service", "hardware not found")

                        if (ContextCompat.checkSelfPermission(this@APICallService, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                            writeToFirebase()
                        }

                    }
                }

                        /*if (hardwareData.Circulation_Pump_Status.equals("False") || hardwareData.Fan_Extractor_Status.equals("False") || hardwareData.Fan_Tent_Status.equals("False")
                            || hardwareData.Light_Status.equals("False")) {
                            title = "Equipment Warning"
                            message = "ERROR: EQUIPMENT OFFLINE"

                            Log.i("Check bg service", "hardware not on")
                        }
                    } else
                        if (hardwareData == null) {
                            title = "hardware data is empty"
                            message = "hardware data is empty"
                            Log.i("Check bg service", "hardware not found")
                        }*/

                    if(title.isNotEmpty() && message.isNotEmpty())
                    {
                        var notification =  createNotification(title,message)
                        startForeground(1,notification)
                    }
                }
        },0,apiCallInterval)

    }

    fun writeToFirebase() {
        val user = User(
            UserID = UserID,
            Username = UserName
        )

        CoroutineScope(Dispatchers.IO).launch {
            // Check if the user is signed in
            val currentUser = FirebaseUtils.Get(user)
            if (currentUser != null) {
                // Create a NotificationDataClass object for the notification
                val notificationData = NotificationDataClass()
                notificationData.notificationType = "System Warning"

                notificationData.notificationMessage = "ERROR: EQUIPMENT NOT FOUND"

                val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                val timestamp = sdf.format(Date()) // Format the current time
                notificationData.timestamp = timestamp

                // Get the User object for the currently signed-in user
                val user = User(UserID, UserName)

                // Insert the notification for the user
                val isInserted = NotificationDataClass.insertNotificationForUser(
                    user,
                    notificationData
                )

                if (isInserted) {
                    Log.i("Notification Insert", "Notification inserted successfully")
                } else {
                    Log.e("Notification Insert", "Failed to insert notification")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

    enum class Actions{
        START,STOP
    }
    private fun createNotification(title: String, message: String): Notification? {

        //Confirm usage of intent

        /*val intent = Intent(applicationContext, GoogleLogin::class.java)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )*/

        val color = ContextCompat.getColor(applicationContext,R.color.red)

        val notification = NotificationCompat.Builder(applicationContext, "Channel_id")
            .setContentTitle(title)
            .setContentText(message)
            .setOngoing(false)
            .setColorized(true)
            .setColor(color)
            .setSmallIcon(R.drawable.ic_notification_danger)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
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




