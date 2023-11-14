package com.example.opsc7312part1

data class Store(
    val name: String = "",
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val placeId: String = "",
    val status: String = "",
    val rating: Int = 0,
    val isOpenNow: Boolean = false,
    var favorite: Boolean = false,
    var notes : String = ""
)
