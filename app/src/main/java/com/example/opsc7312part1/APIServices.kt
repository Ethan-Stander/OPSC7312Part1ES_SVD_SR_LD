package com.example.opsc7312part1

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json


class APIServices {

    companion object {
        private val httpClient = HttpClient()
        private const val JSON_URL_DATA = "your_json_url_here"
        private const val JSON_URL_HARDWARE = "your_json_url_here"

        suspend fun fetchSensorDataFromJson(): SensorData? {
            return withContext(Dispatchers.IO) {
                val response: HttpResponse = httpClient.get(JSON_URL_DATA)
                if (response.status.value == 200) {
                    val json = response.bodyAsText()
                    val sensorData = Json.decodeFromString<SensorData>(json)
                    return@withContext sensorData
                } else {
                    // Handle non-200 response status if needed
                    return@withContext null
                }
            }
        }

        suspend fun fetchHardware(): hardware? {
            return withContext(Dispatchers.IO) {
                val response: HttpResponse = httpClient.get(JSON_URL_HARDWARE)
                if (response.status.value == 200) {
                    val json = response.bodyAsText()
                    val hardware = Json.decodeFromString<hardware>(json)
                    return@withContext hardware
                } else {
                    // Handle non-200 response status if needed
                    return@withContext null
                }
            }
        }

        suspend fun GenericSwitch(URL : String): Boolean {
            return withContext(Dispatchers.IO){
                val response: HttpResponse = httpClient.get(URL)
                if(response.status.value == 200){

                    return@withContext true
                }
                else
                    return@withContext false
            }


        }



    }

}