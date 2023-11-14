package com.example.opsc7312part1

 data class Action(
    var Date: String = "",
    var EquipmentChanged: String = "",
    var PreviousState: String = "",
    var NewState: String = "",
    var IsDeleted : Boolean = false,
    var farmName : String = ""
)