package com.example.opsc7312part1

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.gson.Gson
import io.ktor.client.engine.cio.CIO


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL


class APIServices {

    companion object {
        private const val ip = "http://192.168.1.10"
        private val httpClient = (CIO)
        private const val JSON_URL_SENSOR_DATA = ip+"/sensor"
        private const val JSON_URL_HARDWARE = ip+"/hardware"
        private const val JSON_URL_AI_TOGGLE =  ip+"/toggleAI"
        private const val JSON_URL_AI_PREDICTIONS = ip +"/predictions"


         fun AddSensorDataToDB(context : Context, SensorData : SensorDataAPI)
        {
            val dbHelper = DatabaseHelper(context)
            dbHelper.addSensorData(SensorData)
        }

        fun AddHardwareToDB(context: Context, Hardware : hardware)
        {
            val dbHelper = DatabaseHelper(context)
            dbHelper.addHardware(Hardware)
        }



        @SuppressLint("RestrictedApi")
        suspend fun fetchSensorDataFromJson(context: Context): SensorDataAPI? {
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

                if (data != null) {
                    AddSensorDataToDB(context,data)
                    return@withContext data
                }

                else
                return@withContext null
            }

        }

        suspend fun fetchhardware(context: Context) : hardware?
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
                    Log.i("fetch hardware API error",e.message.toString())
                    return@withContext null
                }
                if(Hardware != null)
                {
                    AddHardwareToDB(context,Hardware)
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
                    connection.requestMethod = "POST"
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
                    Log.i("hardware API error",e.message.toString())
                    // Handle exceptions such as network errors
                    return@withContext false
                }
            }
        }

        suspend fun toggle_AI_Switch() : Boolean
        {
            return withContext(Dispatchers.IO)
            {
                try{
                    val connection = URL(JSON_URL_AI_TOGGLE).openConnection() as HttpURLConnection
                    connection.requestMethod = "POST"
                    connection.connectTimeout = 5000
                    connection.readTimeout = 5000

                    val responseCode = connection.responseCode

                    if(responseCode == 200){
                        return@withContext true
                    } else
                    {
                        return@withContext false
                    }
                } catch (e:Exception) {
                    Log.i("Kill me",e.message.toString())
                }
                return@withContext false
            }
        }

        suspend fun fetchPredictions() : Predictions?
        {
            return withContext(Dispatchers.IO)
            {

                var predictions : Predictions? = null
                try {
                    val url = URL(JSON_URL_AI_PREDICTIONS)
                    val json = url.readText()
                    predictions = Gson().fromJson(json, Predictions::class.java)
                }
                catch (e:Exception)
                {
                    Log.i("fetch predictions API error",e.message.toString())
                    return@withContext null
                }
                if(predictions != null)
                {
                    return@withContext predictions
                }
                else
                    return@withContext null

            }
        }

        suspend fun fetchPredictionstest() : Predictions?
        {
            return withContext(Dispatchers.IO)
            {

                var predictions : Predictions? = null
                try {
                    val url = URL(JSON_URL_AI_TOGGLE)
                    val json = url.readText()
                    predictions = Gson().fromJson(json, Predictions::class.java)
                }
                catch (e:Exception)
                {
                    Log.i("fetch predictions API error",e.message.toString())
                    return@withContext null
                }
                if(predictions != null)
                {
                    return@withContext predictions
                }
                else
                    return@withContext null

            }
        }


    }

}