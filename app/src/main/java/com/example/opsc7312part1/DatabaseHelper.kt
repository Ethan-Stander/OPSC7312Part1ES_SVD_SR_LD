package com.example.opsc7312part1

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: APICallService) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "MyDatabase.db"
        private const val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create the "notifications" table

        val createNotificationsTableQuery = """
            CREATE TABLE IF NOT EXISTS notifications (
                notificationID INTEGER PRIMARY KEY AUTOINCREMENT,
                notificationType TEXT,
                notificationMessage TEXT,
                timestamp TEXT
            )
        """.trimIndent()
        db.execSQL(createNotificationsTableQuery)

        // Create the "hardware" table
        val createHardwareTableQuery = """
            CREATE TABLE IF NOT EXISTS hardware (
                hardwareID INTEGER PRIMARY KEY AUTOINCREMENT,
                pH_Up_Pump TEXT,
                pH_In_Pump_Status TEXT,
                pH_Down_Pump TEXT,
                pH_Out_Pump_Status TEXT,
                EC_Up_Pump TEXT,
                EC_In_Pump_Status TEXT,
                EC_Down_Pump TEXT,
                EC_Out_Pump_Status TEXT,
                Circulation_Pump TEXT,
                Circulation_Pump_Status TEXT,
                Extractor_Fan TEXT,
                Fan_Extractor_Status TEXT,
                Tent_Fan TEXT,
                Fan_Tent_Status TEXT,
                Grow_Light TEXT,
                Light_Status TEXT
            )
        """.trimIndent()
        db.execSQL(createHardwareTableQuery)
    }
    fun addHardware(hardware: hardware) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("pH_Up_Pump", hardware.pH_Up_Pump)
        values.put("pH_In_Pump_Status", hardware.pH_In_Pump_Status)
        values.put("pH_Down_Pump", hardware.pH_Down_Pump)
        values.put("pH_Out_Pump_Status", hardware.pH_Out_Pump_Status)
        values.put("EC_Up_Pump", hardware.EC_Up_Pump)
        values.put("EC_In_Pump_Status", hardware.EC_In_Pump_Status)
        values.put("EC_Down_Pump", hardware.EC_Down_Pump)
        values.put("EC_Out_Pump_Status", hardware.EC_Out_Pump_Status)
        values.put("Circulation_Pump", hardware.Circulation_Pump)
        values.put("Circulation_Pump_Status", hardware.Circulation_Pump_Status)
        values.put("Extractor_Fan", hardware.Extractor_Fan)
        values.put("Fan_Extractor_Status", hardware.Fan_Extractor_Status)
        values.put("Tent_Fan", hardware.Tent_Fan)
        values.put("Fan_Tent_Status", hardware.Fan_Tent_Status)
        values.put("Grow_Light", hardware.Grow_Light)
        values.put("Light_Status", hardware.Light_Status)

        db.insert("hardware", null, values)
        db.close()
    }

    // Add a notification object to the "notifications" table
    fun addNotification(notification: NotificationDataClass) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put("notificationType", notification.notificationType)
        values.put("notificationMessage", notification.notificationMessage)
        values.put("timestamp", notification.timestamp)
        db.insert("notifications", null, values)
        db.close()
    }

    // Retrieve a list of hardware objects from the "hardware" table
    @SuppressLint("Range")
    fun getAllHardware(): List<hardware> {
        val hardwareList = mutableListOf<hardware>()
        val db = this.readableDatabase
        val query = "SELECT * FROM hardware"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val hardware = hardware(
                cursor.getString(cursor.getColumnIndex("pH_Up_Pump")),
                cursor.getString(cursor.getColumnIndex("pH_In_Pump_Status")),
                cursor.getString(cursor.getColumnIndex("pH_Down_Pump")),
                cursor.getString(cursor.getColumnIndex("pH_Out_Pump_Status")),
                cursor.getString(cursor.getColumnIndex("EC_Up_Pump")),
                cursor.getString(cursor.getColumnIndex("EC_In_Pump_Status")),
                cursor.getString(cursor.getColumnIndex("EC_Down_Pump")),
                cursor.getString(cursor.getColumnIndex("EC_Out_Pump_Status")),
                cursor.getString(cursor.getColumnIndex("Circulation_Pump")),
                cursor.getString(cursor.getColumnIndex("Circulation_Pump_Status")),
                cursor.getString(cursor.getColumnIndex("Extractor_Fan")),
                cursor.getString(cursor.getColumnIndex("Fan_Extractor_Status")),
                cursor.getString(cursor.getColumnIndex("Tent_Fan")),
                cursor.getString(cursor.getColumnIndex("Fan_Tent_Status")),
                cursor.getString(cursor.getColumnIndex("Grow_Light")),
                cursor.getString(cursor.getColumnIndex("Light_Status"))
            )
            hardwareList.add(hardware)
        }


        cursor.close()
        db.close()

        return hardwareList
    }

    // Retrieve a list of notification objects from the "notifications" table
    @SuppressLint("Range")
    fun getAllNotifications(): List<NotificationDataClass> {
        val notificationList = mutableListOf<NotificationDataClass>()
        val db = this.readableDatabase
        val query = "SELECT * FROM notifications"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val notification = NotificationDataClass()
            notification.notificationType = cursor.getString(cursor.getColumnIndex("notificationType"))
            notification.notificationMessage = cursor.getString(cursor.getColumnIndex("notificationMessage"))
            notification.timestamp = cursor.getString(cursor.getColumnIndex("timestamp"))
            notificationList.add(notification)
        }


        cursor.close()
        db.close()

        return notificationList
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Handle database upgrades here
    }
}

