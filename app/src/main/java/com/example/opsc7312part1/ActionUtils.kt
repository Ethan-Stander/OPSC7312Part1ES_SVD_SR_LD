package com.example.opsc7312part1

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.google.android.gms.tasks.Tasks



class ActionUtils {

    companion object {
        fun sendActionsToFirebase(actions: List<Action>) {
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("actions")

            actions.forEachIndexed { index, action ->
                val actionRef = myRef.push() // Create a new unique key for each action
                actionRef.setValue(action) // Set the action object as the value
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
