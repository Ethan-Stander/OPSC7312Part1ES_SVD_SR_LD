package com.example.opsc7312part1

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener.Builder.withContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FarmClass {

    var FarmID : String = ""
    var FarmName : String = ""
    var FireBaseID : String = ""


    companion object {
        private val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference
        suspend fun addFarmRecordToFirebase(farm: FarmClass) = withContext(Dispatchers.IO) {
            // Generate a unique key for the new record
            val farmKey = databaseReference.child("farm").push().key

            // Use the generated key to set the value in the "farm" node
            if (farmKey != null) {
                databaseReference.child("farm").child(farmKey).setValue(farm)
            }
        }

        suspend fun getFarmRecordFromFirebase(farmID: String): FarmClass? = withContext(Dispatchers.IO) {
            return@withContext try {
                val dataSnapshot = databaseReference.child("farm").orderByChild("farmID").equalTo(farmID).get().await()

                dataSnapshot.children.firstOrNull()?.let { childSnapshot ->
                    val farmName = childSnapshot.child("farmName").getValue(String::class.java)
                    // Add other fields as needed



                    var farm = FarmClass()
                    if (farmName != null) {
                        farm.FarmName = farmName
                    }
                    farm.FarmID = farmID
                    farm
                }
            } catch (e: Exception) {
                // Handle exceptions if needed
                e.printStackTrace()
                null
            }
        }





    }

}