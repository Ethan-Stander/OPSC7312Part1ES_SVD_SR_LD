package com.example.opsc7312part1

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class FirebaseUtils {
    companion object {
        val database = FirebaseDatabase.getInstance()

        suspend fun insertSettingForUser(user: User, setting: Setting): Boolean {
            val currentUser = user.UserID
            val reference = currentUser?.let { database.getReference("Users").child(it).child("settings") }

            return try {
                if (reference != null) {
                    reference.setValue(setting).await()
                }
                true // Data was successfully written to the database
            } catch (e: Exception) {
                false // Failed to write data to the database
            }
        }

        suspend fun updateSettingForUser(user: User, setting: Setting): Boolean {
            val currentUser = user.UserID
            val reference = currentUser?.let { database.getReference("Users").child(it).child("settings") }

            return try {
                if (reference != null) {
                    reference.setValue(setting).await()
                }
                true // Data was successfully updated in the database
            } catch (e: Exception) {
                false // Failed to update data in the database
            }
        }

        suspend fun Get(user: User): Setting? {
            val currentUser = user.UserID
            val reference = currentUser?.let { database.getReference("Users").child(it).child("settings") }

            return try {
                if (reference != null) {
                    val dataSnapshot = reference.get().await()
                    if (dataSnapshot.exists()) {
                        val setting = dataSnapshot.getValue(Setting::class.java)
                        setting // Return the retrieved setting object
                    } else {
                        null // No data exists for the user's settings
                    }
                } else {
                    null // Reference is null, possibly due to a missing UserID
                }
            } catch (e: Exception) {
                null // An error occurred while retrieving data
            }
        }


    }
}