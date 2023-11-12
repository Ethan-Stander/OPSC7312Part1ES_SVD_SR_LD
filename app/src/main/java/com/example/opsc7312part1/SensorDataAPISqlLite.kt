package com.example.opsc7312part1

data class SensorDataAPISqlLite(
    val Temperature: String?,
    val Humidity: String?,
    val LightLevel: String?,
    val FlowRate: String?,
    val pH: String?,
    val EC: String?,
    val timeCalled: String?,
    val isDeleted: Boolean?,
    val farmName: String
)