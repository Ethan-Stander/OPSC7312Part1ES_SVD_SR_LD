package com.example.opsc7312part1

data class SensorDataAPISqlLite(
    var Temperature: String = "",
    var Humidity: String = "",
    var LightLevel: String = "",
    var FlowRate: String = "",
    var pH: String = "",
    var EC: String = "",
    var timeCalled: String = "",
    var isDeleted: Boolean = false,
    var farmName: String  = ""

) {
    fun updateFromFirebase(other: SensorDataAPISqlLite) {
        Temperature = other.Temperature.takeIf { it.isNotEmpty() } ?: Temperature
        Humidity = other.Humidity.takeIf { it.isNotEmpty() } ?: Humidity
        LightLevel = other.LightLevel.takeIf { it.isNotEmpty() } ?: LightLevel
        FlowRate = other.FlowRate.takeIf { it.isNotEmpty() } ?: FlowRate
        pH = other.pH.takeIf { it.isNotEmpty() } ?: pH
        EC = other.EC.takeIf { it.isNotEmpty() } ?: EC
        timeCalled = other.timeCalled.takeIf { it.isNotEmpty() } ?: timeCalled
        isDeleted = other.isDeleted || isDeleted
        farmName = other.farmName.takeIf { it.isNotEmpty() } ?: farmName
    }
}
