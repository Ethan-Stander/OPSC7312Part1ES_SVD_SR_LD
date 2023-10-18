import android.content.Intent
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
        val name: TextView = itemView.findViewById(R.id.txtPlaceName)
        val phone: TextView = itemView.findViewById(R.id.txtPlacePhone)
        val rating: TextView = itemView.findViewById(R.id.txtPlaceRating)
        val directionsButton: Button = itemView.findViewById(R.id.btnDirections)
        val AddToMyStoresButton: Button = itemView.findViewById(R.id.btnAddmyStores)

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
        holder.phone.text = currentItem.phone
        holder.rating.text = currentItem.rating.toString()

        holder.directionsButton.setOnClickListener {
            DirLat = currentItem.latitude
            DirLong = currentItem.longitude
            fragmentNavigation.replaceFragment(Place_Directions(fragmentNavigation), "map")
        }

        holder.AddToMyStoresButton.setOnClickListener {
            val context = holder.itemView.context
            // Disable the button immediately after being clicked
            it.isEnabled = false

            // Check if the item exists in the database
            databaseReference.child(currentItem.placeId).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        // If the item doesn't exist, add it to the database
                        databaseReference.child(currentItem.placeId).setValue(currentItem)
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle potential errors here
                    it.isEnabled = true
                }
            })
        }
    }
}

