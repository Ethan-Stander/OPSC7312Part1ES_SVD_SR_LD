package com.example.opsc7312part1

import com.google.firebase.database.FirebaseDatabase

class FeedbackUtil {

    companion object {
        fun sendFeedbackToFirebase(feedback: List<FeedbackData>) {
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("feedback")

            feedback.forEachIndexed { index, feedback ->
                val feedbackRef = myRef.push()
                feedbackRef.setValue(feedback)
            }
        }
    }
}