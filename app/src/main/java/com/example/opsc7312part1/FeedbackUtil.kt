package com.example.opsc7312part1

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class FeedbackUtil {

    companion object {

        private val database = FirebaseDatabase.getInstance()
        private val feedbackReference: DatabaseReference = database.getReference("feedback")


        fun sendFeedbackToFirebase(feedback: List<FeedbackData>) {
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("feedback")

            feedback.forEachIndexed { index, feedbackData ->
                val feedbackRef = myRef.push()
                feedbackData.id = feedbackRef.key!!
                feedbackRef.setValue(feedbackData)
            }
        }




        suspend fun getFeedbackForAdmin() : List<FeedbackData> {
            return try {
                val snapshot = feedbackReference?.get()?.await()
                val feedbackList = mutableListOf<FeedbackData>()

                if (snapshot != null) {
                    if (snapshot.exists()) {
                        for (dataSnapshot in snapshot.children) {
                            val feedback = dataSnapshot.getValue(FeedbackData::class.java)
                            if (feedback != null) {
                                feedbackList.add(feedback)
                            }
                        }
                    }
                } else {
                    Log.e("FeedbackUtil", "Snapshot is null")
                }

                feedbackList
            } catch (e: Exception) {
                Log.e("FeedbackUtil", "Error getting feedback", e)
                emptyList()
            }
        }

        suspend fun deleteFeedbackById(feedbackId: String): Boolean {
            return try {
                val feedbackRef = feedbackReference.child(feedbackId)
                feedbackRef.removeValue().await()
                Log.d("FeedbackUtil", "Feedback with ID $feedbackId deleted successfully")
                true
            } catch (e: Exception) {
                Log.e("FeedbackUtil", "Error deleting feedback", e)
                false
            }
        }

    }
}