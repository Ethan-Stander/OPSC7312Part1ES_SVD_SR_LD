package com.example.opsc7312part1

import com.google.firebase.database.FirebaseDatabase

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
    }

}