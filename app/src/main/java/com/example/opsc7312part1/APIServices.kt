package com.example.opsc7312part1

import android.provider.ContactsContract.CommonDataKinds.Website.URL
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
import java.net.URL


class APIServices {

    companion object {
        private val httpClient = (CIO)
        private const val JSON_URL_DATA = "192.168.1.10/sensor.json"
        private const val JSON_URL_HARDWARE = "192.168.1.10/hardware.json"

        suspend fun fetchSensorDataFromJson(): SensorDataAPI? {
            return withContext(Dispatchers.IO)
            {

                var data: SensorDataAPI? = null
                try {
                    val url = URL(JSON_URL_DATA);
                    val json = url.readText()
                     data = Gson().fromJson(json, SensorDataAPI::class.java)
                }
                catch(e:Exception)
                {
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





    }

}