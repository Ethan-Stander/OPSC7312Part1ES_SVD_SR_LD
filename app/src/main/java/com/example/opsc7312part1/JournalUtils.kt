package com.example.opsc7312part1

import android.net.Uri
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.net.URI

class JournalUtils {

    companion object {

        val database = FirebaseDatabase.getInstance()
        var storage = FirebaseStorage.getInstance().reference.child("Images")
        suspend fun saveJournalEntry(user: User, journalEntry: Journal): Boolean {
            val currentUser = user.UserID
            val reference =
                currentUser?.let { FirebaseUtils.database.getReference("Users").child(it).child("journalEntries")}

            return try {
                if (reference != null) {
                    val entryRef = reference.push()
                    entryRef.setValue(journalEntry).await()
                }
                true
            } catch (e: Exception) {
                false
            }
        }

        fun saveJournalImage(imageURI : Uri?, callback: (String) -> Unit) {
            imageURI?.let {
                val fileReference = storage.child(System.currentTimeMillis().toString())
                fileReference.putFile(it).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        fileReference.downloadUrl.addOnSuccessListener { uri ->
                            val photoLink = uri.toString()
                            callback(photoLink)
                        }
                    } else {
                        Log.i("image upload failed: ", task.exception?.message.toString())
                        callback("")
                    }
                }
            } ?: callback("")
        }


    }
}