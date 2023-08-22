package com.example.opsc7312part1

import android.provider.ContactsContract.CommonDataKinds.Website.URL
import android.util.Log
import com.google.gson.Gson
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import kotlinx.coroutines.runBlocking
import java.net.HttpURLConnection
import java.net.URL


class APIServices {

    companion object {
        private const val ip = "http://192.168.1.10"
        //private const val ip = "http://100.66.16.44"
        private val httpClient = (CIO)
        private const val JSON_URL_SENSOR_DATA = ip+"/sensor.json"
        private const val JSON_URL_HARDWARE = ip+"/hardware.json"

        /*"http://192.168.1.10/hardware.json"*/

        suspend fun fetchSensorDataFromJson(): SensorDataAPI? {
            return withContext(Dispatchers.IO)
            {

                var data: SensorDataAPI? = null
                try {
                    val url = URL(JSON_URL_SENSOR_DATA);
                    val json = url.readText()
                     data = Gson().fromJson(json, SensorDataAPI::class.java)
                }
                catch(e:Exception)
                {
                    Log.i("Execption message",e.message.toString())
                    return@withContext null
                }

            if(data != null) {
                return@withContext data
            }
            else
                return@withContext null
            }

        }

        suspend fun fetchhardware() : hardware?
        {
            return withContext(Dispatchers.IO)
            {

                var Hardware : hardware? = null
                try {
                    val url = URL(JSON_URL_HARDWARE)
                    val json = url.readText()
                    Hardware = Gson().fromJson(json, hardware::class.java)
                }
                catch (e:Exception)
                {
                    return@withContext null
                }
                if(Hardware != null)
                {
                    return@withContext Hardware
                }
                else
                    return@withContext null
            }
        }

        suspend fun Switch(URL : String) : hardware?
        {
            return withContext(Dispatchers.IO)
            {

                var Hardware : hardware? = null
                try {
                    val url = URL(URL)
                    val json = url.readText()
                    Hardware = Gson().fromJson(json, hardware::class.java)
                }
                catch (e:Exception)
                {
                    return@withContext null
                }
                if(Hardware != null)
                {
                    return@withContext Hardware
                }
                else
                    return@withContext null
            }
        }

        suspend fun switch(url: String): Boolean {
            return withContext(Dispatchers.IO) {
                try {
                    val connection = URL(url).openConnection() as HttpURLConnection
                    connection.requestMethod = "GET"
                    connection.connectTimeout = 5000 // Set your desired timeout
                    connection.readTimeout = 5000 // Set your desired timeout

                    // Get the HTTP response code
                    val responseCode = connection.responseCode

                    // Check if the response code is 200 (OK)
                    if (responseCode == 200) {
                        return@withContext true
                    } else {
                        // Handle other response codes if needed
                        return@withContext false
                    }
                } catch (e: Exception) {
                    // Handle exceptions such as network errors
                    return@withContext false
                }
            }
        }






    }

}