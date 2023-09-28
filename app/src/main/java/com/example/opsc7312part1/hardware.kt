package com.example.opsc7312part1

data class hardware(
    val pH_Up_Pump: String?,
    var pH_In_Pump_Status: String,
    val pH_Down_Pump: String?,
    var pH_Out_Pump_Status: String,
    val EC_Up_Pump: String?,
    var EC_In_Pump_Status: String,
    val EC_Down_Pump: String?,
    var EC_Out_Pump_Status: String,
    val Circulation_Pump: String?,
    var Circulation_Pump_Status: String,
    val Extractor_Fan: String?,
    var Fan_Extractor_Status: String,
    val Tent_Fan: String?,
    var Fan_Tent_Status: String,
    val Grow_Light: String?,
    var Light_Status: String,

    ) {
    fun setValues() {
        pH_In_Pump_Status = if (pH_Up_Pump == "0") "True" else "False"
        pH_Out_Pump_Status = if (pH_Down_Pump == "0") "True" else "False"
        EC_In_Pump_Status = if (EC_Up_Pump == "0") "True" else "False"
        EC_Out_Pump_Status = if (EC_Down_Pump == "0") "True" else "False"
        Circulation_Pump_Status = if (Circulation_Pump == "0") "True" else "False"
        Fan_Extractor_Status = if (Extractor_Fan == "0") "True" else "False"
        Fan_Tent_Status = if (Tent_Fan == "0") "True" else "False"
        Light_Status = if (Grow_Light == "0") "True" else "False"
    }


fun getAllStatuses(): Array<String> {
    return arrayOf(
        pH_In_Pump_Status,
        pH_Out_Pump_Status,
        EC_In_Pump_Status,
        EC_Out_Pump_Status,
        Circulation_Pump_Status,
        Fan_Extractor_Status,
        Fan_Tent_Status,
        Light_Status
    )

}



companion object {


    val attributeNames = arrayOf(
        "pH_In_Pump",
        "pH_Out_Pump",
        "EC_In_Pump",
        "EC_Out_Pump",
        "Circulation_Pump",
        "Fan_Extractor",
        "Fan_Tent",
        "Light"
    )


    val ip = "http://192.168.1.10";
    val links = arrayOf(
        "$ip/ph_in.json",
        "$ip/ph_out.json",
        "$ip/ec_in.json",
        "$ip/ec_out.json",
        "$ip/circ_pump.json",
        "$ip/fan_extractor.json",
        "$ip/fan_circ.json",
        "$ip/light.json",
        )

    }
}

