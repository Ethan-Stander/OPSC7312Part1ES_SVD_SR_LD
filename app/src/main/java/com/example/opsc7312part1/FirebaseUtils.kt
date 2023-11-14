package com.example.opsc7312part1

import android.util.Log
import com.google.android.gms.tasks.Tasks
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
                    // Remove any existing data under "sensor_data"
                    myRef.removeValue()

                    // Add the new sensor data directly under "sensor_data" with generated keys
                    sensorDataList.forEach { sensorData ->
                        Log.d("Debug", "Entering forEach loop")
                        val sensorDataRef = myRef.push()
                        sensorDataRef.setValue(sensorData)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }



        suspend fun getSensorDataFromFirebase(): List<SensorDataAPISqlLite> = withContext(Dispatchers.IO) {
            try {
                val database = FirebaseDatabase.getInstance().getReference("sensor_data")
                val task = database.get()

                // Suspend the coroutine until the task is complete
                val snapshot = Tasks.await(task)

                if (snapshot != null) {
                    Log.d("DataTag", "Snapshot exists: ${snapshot.exists()}")
                }
                val sensorDataList = mutableListOf<SensorDataAPISqlLite>()

                if (snapshot != null && snapshot.exists()) {
                    for (childSnapshot in snapshot.children) {
                        val sensorData = childSnapshot.getValue(SensorDataAPISqlLite::class.java)
                        if (sensorData != null) {
                            // Update the localSensorData with values from Firebase
                            val localSensorData = SensorDataAPISqlLite()
                            localSensorData.updateFromFirebase(sensorData)
                            sensorDataList.add(localSensorData)
                        }
                    }
                }
                sensorDataList

            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }

        suspend fun fetchActions(): List<Action> = withContext(Dispatchers.IO) {
            try {
                val database = FirebaseDatabase.getInstance().getReference("actions")
                val task = database.get()

                // Suspend the coroutine until the task is complete
                val snapshot = Tasks.await(task)

                if (snapshot != null) {
                    Log.d("YourTag", "Snapshot exists: ${snapshot.exists()}")
                }

                val storesList = mutableListOf<Action>()

                if (snapshot != null && snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val action = dataSnapshot.getValue(Action::class.java)
                        if (action != null) {
                            storesList.add(action)
                        }
                    }
                }

                storesList

            } catch (e: Exception) {
                Log.e("YourTag", "Error fetching actions", e)
                emptyList() // Return an empty list if there was an error
            }
        }

    }

    }
