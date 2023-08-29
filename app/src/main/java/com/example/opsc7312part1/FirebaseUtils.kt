package com.example.opsc7312part1

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class FirebaseUtils {
    companion object {
        private val database = FirebaseDatabase.getInstance()

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

    }
}