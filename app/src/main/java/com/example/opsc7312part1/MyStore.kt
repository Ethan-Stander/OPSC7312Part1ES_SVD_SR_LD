package com.example.opsc7312part1

import com.google.firebase.database.*
import kotlinx.coroutines.tasks.await

class MyStore (
    var name: String = "",
    var address: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var placeId: String = "",
    var status: String = "",
    var rating: Int = 0,
    var isOpenNow: Boolean = false,
    var favorite: Boolean = false
)
{
    companion object {
        private val database = FirebaseDatabase.getInstance()

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
            val reference = currentUser?.let { database.getReference("Users").child(it).child("mystores") }

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
    }
}
