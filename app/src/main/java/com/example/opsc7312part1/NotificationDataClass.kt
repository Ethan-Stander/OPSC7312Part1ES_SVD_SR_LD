package com.example.opsc7312part1

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await

class Notification {
    var notificationType: String? = null
    var notificationMessage: String? = null
    var timestamp: Long? = null

    companion object{

        val database = FirebaseDatabase.getInstance()

        suspend fun insertNotificationForUser(user: User, notification: Notification) : Boolean {
            val currentUser = user.UserID
            val reference = currentUser?.let { database.getReference("Users").child(it).child("notifications") }

            return try {
                if (reference != null) {
                    reference.setValue(notification).await()
                }
                true // Data was successfully written to the database
            } catch (e: Exception) {
                false // Failed to write data to the database
            }
        }

        suspend fun getNotificationsForUser(user: User): MutableList<Notification>? {
            val currentUser = user.UserID
            val reference = currentUser?.let { database.getReference("Users").child(it).child("notifications") }

            val notifications = mutableListOf<Notification>()

            try {
                reference?.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (childSnapshot in snapshot.children) {
                            val notification = childSnapshot.getValue(Notification::class.java)
                            notification?.let { notifications.add(it) }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                      return
                    }
                })
            } catch (e: Exception) {
                return null
            }

            return notifications
        }






    }
    }


