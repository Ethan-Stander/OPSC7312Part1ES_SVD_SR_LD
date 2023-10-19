package com.example.opsc7312part1

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MyStoresAdapter(private var myStoreList : List<MyStore>): RecyclerView.Adapter<MyStoresAdapter.ViewHolder>() {

    fun setFilteredist(storeList: List<MyStore>){
        this.myStoreList = storeList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val storeDetails : TextView = itemView.findViewById(R.id.txtStoreDetails)
        val storeFavorite : ImageView = itemView.findViewById(R.id.btnFavorite)
        val btnViewStoreOnMap : Button = itemView.findViewById(R.id.btnViewOnMap)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.my_stores_details_list,parent,false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return myStoreList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = myStoreList[position]
        holder.storeDetails.text = currentItem.name +  "\n" +currentItem.address + "\n" + currentItem.rating

        val favoriteDrawable = if (currentItem.favorite) R.drawable.ic_favorite else R.drawable.ic_not_favorite
        holder.storeFavorite.setImageResource(favoriteDrawable)

        holder.storeFavorite.setOnClickListener {
            currentItem.favorite = !currentItem.favorite
            val newFavoriteDrawable = if (currentItem.favorite) R.drawable.ic_favorite else R.drawable.ic_not_favorite
            holder.storeFavorite.setImageResource(newFavoriteDrawable)


            val scope = CoroutineScope(Dispatchers.Main)
            val job = scope.launch {
                UserID?.let { MyStore.updateStoreWithPlaceIdFav(it, currentItem.placeId, currentItem)
                    val user = User(
                        UserID = UserID,
                        Username = UserName
                    )
                    myStoreList = MyStore.getStoresForUser(user)
                }

            }





        }

        holder.storeDetails.setOnClickListener {
            val dialogFragment = myStoreDetailPopUp(currentItem)
            dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.settings_custom_popups)
            dialogFragment.show((holder.itemView.context as AppCompatActivity).supportFragmentManager, "MyStoreDetailDialog")
        }

        holder.btnViewStoreOnMap.setOnClickListener {
            TODO("Not yet implemented")
        }

    }

}
