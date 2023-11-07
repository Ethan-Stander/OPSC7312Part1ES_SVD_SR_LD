package com.example.opsc7312part1

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class JournalUtils {

    companion object {

        val database = FirebaseDatabase.getInstance()
        suspend fun saveJournalEntry(user: User, journalEntry: Journal): Boolean {
            val currentUser = user.UserID
            val reference =
                currentUser?.let { FirebaseUtils.database.getReference("Users").child(it).child("journalEntries") }

            return try {
                if (reference != null) {
                    reference.setValue(journalEntry).await()
                }
                true
            } catch (e: Exception) {
                false
            }
        }
    }
}