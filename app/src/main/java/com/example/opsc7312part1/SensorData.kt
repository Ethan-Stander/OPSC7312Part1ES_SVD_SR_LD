package com.example.opsc7312part1

data class SensorData(
    var sensorTitle: String,
    var sensorOptimalRange: String,
    var sensorCurrentReading: String,
    var status: Boolean
) {
    fun isReadingWithinOptimalRange(): String {
        val rangeValues = sensorOptimalRange.split("-")
        if (rangeValues.size != 2) {
            // Invalid optimal range format
            return "false"
        }

        val minValue = rangeValues[0].toDoubleOrNull()
        val maxValue = rangeValues[1].toDoubleOrNull()
        val currentReading = sensorCurrentReading.toDoubleOrNull()

        if (minValue == null || maxValue == null || currentReading == null) {
            // Invalid numeric values
            return "false"
        }

        return (currentReading >= minValue && currentReading <= maxValue).toString()
    }
}


