package com.example.opsc7312part1

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class ActionUtils {

    companion object {
        fun sendActionsToFirebase(actions: List<Action>) {
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("actions")

            actions.forEachIndexed { index, action ->
                val actionRef = myRef.push() // Create a new unique key for each action
                actionRef.setValue(action) // Set the action object as the value
            }


            suspend fun fetchActions(): List<Action> {
                 val database = FirebaseDatabase.getInstance()
                 val actionsRef: DatabaseReference = database.getReference("actions")
                return suspendCoroutine { continuation ->
                    val valueListener = object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val actionsList = mutableListOf<Action>()

                            for (actionSnapshot in snapshot.children) {
                                val action = actionSnapshot.getValue(Action::class.java)
                                action?.let { actionsList.add(it) }
                            }

                            continuation.resume(actionsList)
                            actionsRef.removeEventListener(this)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            continuation.resumeWithException(error.toException())
                            actionsRef.removeEventListener(this)
                        }
                    }

                    actionsRef.addListenerForSingleValueEvent(valueListener)
                }
            }


        }
    }

}