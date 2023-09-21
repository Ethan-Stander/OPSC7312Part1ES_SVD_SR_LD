package com.example.opsc7312part1

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await

class NotificationDataClass {
    var notificationType: String? = null
    var notificationMessage: String? = null
    var timestamp: String? = null


    companion object{

        val database = FirebaseDatabase.getInstance()

        suspend fun insertNotificationForUser(user: User, notification: NotificationDataClass): Boolean {
            return try {
                val currentUser = user.UserID
                val reference = currentUser?.let { database.getReference("Users").child(it).child("notifications") }

                // Get the current count of notifications
                val snapshot = reference?.get()?.await()
                val notificationCount = snapshot?.childrenCount?.toInt()

                // Check if the count exceeds the limit (e.g., 10)
                if (notificationCount != null) {
                    if (notificationCount >= 10) {
                        // If there are more than 10 notifications, remove the oldest one
                        val oldestNotification = snapshot.children.first()
                        oldestNotification.ref.removeValue().await()
                    }
                }

                if (reference != null) {
                    // Generate a unique key for each notification
                    val notificationKey = reference.push().key
                    if (notificationKey != null) {

                        // Set the new notification under the generated key
                        reference.child(notificationKey).setValue(notification).await()
                    }
                }
                true // Data was successfully written to the database
            } catch (e: Exception) {
                false // Failed to write data to the database
            }
        }

        suspend fun getNotificationsForUser(user: User): MutableList<NotificationDataClass>? {
            val currentUser = user.UserID
            val reference = currentUser?.let { database.getReference("Users").child(it).child("notifications") }

            val notifications = mutableListOf<NotificationDataClass>()

            return try {
                val dataSnapshot = reference?.get()?.await() // Use get().await() to retrieve data

                dataSnapshot?.children?.forEach { childSnapshot ->
                    val notification = childSnapshot.getValue(NotificationDataClass::class.java)
                    notification?.let { notifications.add(it) }
                }

                notifications // Return the populated list of notifications
            } catch (e: Exception) {
                null // Handle exceptions or errors appropriately
            }
        }

        suspend fun deleteAllNotificationsForUser(user: User): Boolean {
            val currentUser = user.UserID
            val reference = currentUser?.let { database.getReference("Users").child(it).child("notifications") }

            return try {
                reference?.removeValue()?.await() // Use removeValue().await() to delete all notifications

                true // Deletion was successful
            } catch (e: Exception) {
                false // Handle exceptions or errors appropriately
            }
        }

    }
}


