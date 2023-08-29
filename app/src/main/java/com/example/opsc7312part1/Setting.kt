package com.example.opsc7312part1

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

data class Setting(
    var LightTheme: Boolean = true,
    var LocationPermission: Boolean = false,
    var Notifications: Boolean = true,
    var Celsius: Boolean = true,
    var KM: Boolean = true,
    var MaxDistance : Float = 10f
)
