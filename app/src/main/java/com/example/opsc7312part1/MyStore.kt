package com.example.opsc7312part1

import android.util.Log
import com.google.firebase.database.*
import kotlinx.coroutines.tasks.await

class MyStore (
    val name: String = "",
    val address: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val placeId: String = "",
    val status: String = "",
    val rating: Int = 0,
    val isOpenNow: Boolean = false,
    var favorite: Boolean = false,
    var notes : String = ""
    )
{
    companion object {
        private val database = FirebaseDatabase.getInstance()
        private val usersReference: DatabaseReference = database.getReference("Users")

        suspend fun addStoreToFirebase(store: MyStore, user: User): Boolean {
            val currentUser = user.UserID
            val reference = currentUser?.let { database.getReference("Users").child(it).child("mystores") }
            val newStoreReference: DatabaseReference? = reference?.push()

            return try {
                if (newStoreReference != null) {
                    newStoreReference.setValue(store).await()
                }
                true // Data was successfully written to the database
            } catch (e: Exception) {
                false // Failed to write data to the database
            }
        }

        suspend fun removeStoreFromFirebase(store: MyStore, user: User): Boolean {
            val currentUser = user.UserID
            val reference = currentUser?.let { database.getReference("Users").child(it).child("mystores") }

            // Query to find the store with the specified properties (e.g., matching storeName)
            val query: Query? = reference?.orderByChild("storeName")?.equalTo(store.name)

            return try {
                val snapshot = query?.get()?.await()

                if (snapshot?.exists() == true) {
                    // Loop through the results (there might be multiple matching stores)
                    if (snapshot != null) {
                        for (dataSnapshot in snapshot.children) {
                            dataSnapshot.ref.removeValue().await()
                        }
                    }
                    true // Data was successfully removed from the database
                } else {
                    false // Store not found
                }
            } catch (e: Exception) {
                false // Failed to remove data from the database
            }
        }

        suspend fun getStoresForUser(user: User): List<MyStore> {
            val currentUser = user.UserID
            val reference = currentUser?.let { database.getReference("Users").child(it).child("MyStores") }

            return try {
                val snapshot = reference?.get()?.await()
                val storesList = mutableListOf<MyStore>()

                if (snapshot != null) {
                    if (snapshot.exists()) {
                        for (dataSnapshot in snapshot.children) {
                            val store = dataSnapshot.getValue(MyStore::class.java)
                            if (store != null) {
                                storesList.add(store)
                            }
                        }
                    }
                }

                storesList
            } catch (e: Exception) {
                emptyList() // Return an empty list if there was an error
            }
        }

        fun updateStoreWithPlaceId(userId: String, placeId: String, updatedStore: MyStore) {
            val userStoresReference = usersReference.child(userId).child("MyStores")

            userStoresReference.orderByChild("placeId").equalTo(placeId).get()
                .addOnSuccessListener { snapshot ->
                    for (childSnapshot in snapshot.children) {
                        val storeKey = childSnapshot.key
                        if (storeKey != null) {
                            val existingStore = childSnapshot.getValue(MyStore::class.java)
                            if (existingStore != null) {
                                // Check if the existing store's placeId matches the target placeId
                                if (existingStore.placeId == placeId) {
                                    // Update the existing store with the new data
                                    userStoresReference.child(storeKey).setValue(updatedStore)
                                        .addOnSuccessListener {
                                            // Store updated successfully
                                            Log.i("UpdateStoreWithPlaceId", "Store updated successfully")
                                        }
                                        .addOnFailureListener { e ->
                                            // Handle the update failure
                                            Log.e("UpdateStoreWithPlaceId", "Store update failed: ${e.message}")
                                        }
                                }
                            }
                        }
                    }
                }
                .addOnFailureListener { e ->
                    // Handle the query failure
                    Log.e("UpdateStoreWithPlaceId", "Query failed: ${e.message}")
                }
        }

        fun updateStoreWithPlaceIdFav(userId: String, placeId: String, updatedStore: MyStore) {
            val userStoresReference = usersReference.child(userId).child("MyStores")
            userStoresReference.orderByChild("placeId").equalTo(placeId).get()
                .addOnSuccessListener { snapshot ->
                    for (childSnapshot in snapshot.children) {
                        val storeKey = childSnapshot.key
                        if (storeKey != null) {
                            val existingStore = childSnapshot.getValue(MyStore::class.java)
                            if (existingStore != null) {
                                // Check if the existing store's placeId matches the target placeId
                                if (existingStore.placeId == placeId) {
                                    // Update the existing store with the new data
                                    userStoresReference.child(storeKey).setValue(updatedStore)
                                        .addOnSuccessListener {
                                            // Store updated successfully
                                            Log.i("UpdateStoreWithPlaceId", "Store updated successfully")
                                        }
                                        .addOnFailureListener { e ->
                                            // Handle the update failure
                                            Log.e("UpdateStoreWithPlaceId", "Store update failed: ${e.message}")
                                        }
                                }
                            }
                        }
                    }
                }
                .addOnFailureListener { e ->
                    // Handle the query failure
                    Log.e("UpdateStoreWithPlaceId", "Query failed: ${e.message}")
                }
        }

    }
}
