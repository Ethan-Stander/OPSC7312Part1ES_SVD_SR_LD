package com.example.opsc7312part1

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FirebaseUtils {
    companion object {
        val database = FirebaseDatabase.getInstance()

        suspend fun insertSettingForUser(user: User, setting: Setting): Boolean {
            val currentUser = user.UserID
            val reference =
                currentUser?.let { database.getReference("Users").child(it).child("settings") }

            return try {
                if (reference != null) {
                    reference.setValue(setting).await()
                }
                true
            } catch (e: Exception) {
                false
            }
        }

        suspend fun deleteUser(user: User): Boolean {
            val currentUser = user.UserID
            val reference = currentUser?.let { database.getReference("Users").child(it) }

            return try {
                if (reference != null) {
                    reference.removeValue().await()
                }
                true // Data was successfully deleted from the database
            } catch (e: Exception) {
                false // Failed to delete data from the database
            }
        }


        suspend fun updateSettingForUser(user: User, setting: Setting): Boolean {
            val currentUser = user.UserID
            val reference =
                currentUser?.let { database.getReference("Users").child(it).child("settings") }

            return try {
                if (reference != null) {
                    reference.setValue(setting).await()
                }
                true // Data was successfully updated in the database
            } catch (e: Exception) {
                false // Failed to update data in the database
            }
        }

        suspend fun isAdminUser(userID: String): Boolean = withContext(Dispatchers.IO) {
            val reference = database.getReference("Users").child(userID)

            return@withContext try {
                val dataSnapshot = reference.get().await()
                if (dataSnapshot.exists()) {
                    val adminString = dataSnapshot.child("admin").getValue(String::class.java) ?: "false"
                    // Assuming 'admin' is a String property in the User class

                    if(adminString == "True")
                        return@withContext true
                    else
                        return@withContext false
                    // Convert the string value to a boolean and return directly
                } else {
                    false
                }
            } catch (e: Exception) {
                Log.i("get user error", e.message.toString())
                false
            }
        }


        suspend fun Get(user: User): Setting? {
            val currentUser = user.UserID
            val reference =
                currentUser?.let { database.getReference("Users").child(it).child("settings") }

            return try {
                if (reference != null) {
                    val dataSnapshot = reference.get().await()
                    if (dataSnapshot.exists()) {
                        val setting = dataSnapshot.getValue(Setting::class.java)
                        // Returns the setting object
                        setting
                    } else {
                        null
                    }
                } else {
                    null
                }
            } catch (e: Exception) {
                Log.i("get setting error", e.message.toString())
                null
            }
        }

        suspend fun sendSensorDataToFirebase(sensorDataList: List<SensorDataAPISqlLite>) {
            withContext(Dispatchers.IO) {
                val myRef = database.getReference("sensor_data")

                try {
                    val sensorDataNode = myRef.push()
                    sensorDataList.forEachIndexed { index, sensorData ->
                        Log.d("Debug", "Entering forEach loop")
                        val sensorDataRef = sensorDataNode.child("sensor_$index")
                        sensorDataRef.setValue(sensorData)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        suspend fun getSensorDataFromFirebase(): List<SensorDataAPISqlLite> = withContext(Dispatchers.IO) {
            val myRef = database.getReference("sensor_data")

            return@withContext try {
                val dataSnapshot = myRef.get().await()

                val sensorDataList = mutableListOf<SensorDataAPISqlLite>()
                for (childSnapshot in dataSnapshot.children) {
                    val sensorData = childSnapshot.getValue(SensorDataAPISqlLite::class.java)
                    sensorData?.let {
                        sensorDataList.add(it)
                    }
                }

                sensorDataList
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }

    }

    }
