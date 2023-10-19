import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.opsc7312part1.FragmentNavigation
import com.example.opsc7312part1.FragmentTesting
import com.example.opsc7312part1.Nearby_Stores
import com.example.opsc7312part1.Place_Directions
import com.example.opsc7312part1.R
import com.example.opsc7312part1.UserID
import com.example.opsc7312part1.map_nearby
import com.example.opsc7312part1.nearbyStoresList
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


var DirLat:Double = 0.0
var DirLong :Double = 0.0
class PlacesAdapter (private val fragmentNavigation: FragmentNavigation): RecyclerView.Adapter<PlacesAdapter.ViewHolder>() {


    private val databaseReference =
        FirebaseDatabase.getInstance().getReference("Users/$UserID/MyStores")

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Define UI elements here (e.g., TextViews)

        val name: TextView = itemView.findViewById(R.id.txtNearbyStoreItemName)
        val status: TextView = itemView.findViewById(R.id.txtNearbyStoreOperational)
        val rating: TextView = itemView.findViewById(R.id.txtNearbyStoreRating)
        val isOpen: TextView = itemView.findViewById(R.id.txtNearbyStoreisOpen)
        val directionsButton: Button = itemView.findViewById(R.id.btnNearbyStoresDirections)
        val AddToMyStoresButton: Button = itemView.findViewById(R.id.btnNearbyStoresAddtoMyStores)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.store_item, parent, false)
        return ViewHolder(itemView)

    }

    override fun getItemCount(): Int {
        return nearbyStoresList.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = nearbyStoresList[position]

        holder.name.text = currentItem.name
        holder.rating.text = "Rating: "+currentItem.rating.toString()

        if (currentItem.isOpenNow) {
            holder.isOpen.text = "Currently Open"
        }else{
            holder.isOpen.text = "Currently Closed"
        }

        holder.status.text = "Status: "+currentItem.status
        //if exists in my store disable add too my stores
        databaseReference.child(currentItem.placeId).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // disable button if item exists
                    holder.AddToMyStoresButton.isEnabled = false
                    holder.AddToMyStoresButton.text = "Saved"
                } else
                {
                    holder.AddToMyStoresButton.isEnabled = true
                    holder.AddToMyStoresButton.text = "Add to my Stores"
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Error",databaseError.message.toString())
                holder.AddToMyStoresButton.isEnabled = true
                holder.AddToMyStoresButton.text = "Add to my Stores"
            }
        })

        holder.directionsButton.setOnClickListener {
            DirLat = currentItem.latitude
            DirLong = currentItem.longitude
            fragmentNavigation.replaceFragment(Place_Directions(fragmentNavigation), "Directions")
        }

        holder.AddToMyStoresButton.setOnClickListener {
            val context = holder.itemView.context
            // Disable the button immediately after being clicked
            it.isEnabled = false
            holder.directionsButton.text = "Saved"
            databaseReference.child(currentItem.placeId).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        // If the item doesn't exist, add it to the database
                        databaseReference.child(currentItem.placeId).setValue(currentItem)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e("Error",databaseError.message.toString())
                    it.isEnabled = true
                }
            })
        }
    }
}

