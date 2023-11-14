package com.example.opsc7312part1

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class JournalEntryAdapter(private var journalEntryList: List<Journal>):RecyclerView.Adapter<JournalEntryAdapter.ViewHolder>() {

    fun setFilteredList(entryList: List<Journal>){
        this.journalEntryList = entryList
        notifyDataSetChanged()
    }
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val currentEntryTitle : TextView = itemView.findViewById(R.id.txtJournalTitle)
        val currentEntryDate : TextView = itemView.findViewById(R.id.txtJournalDate)
        val entryImage : ImageView = itemView.findViewById(R.id.imgEntry)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.journal_entry_list_item_view,parent,false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return journalEntryList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentEntry = journalEntryList[position]
        holder.currentEntryTitle.text = "${currentEntry.title}"
        holder.currentEntryDate.text = "${currentEntry.date}"
        Picasso.get().load(Uri.parse(currentEntry.imageLink)).into(holder.entryImage)

        holder.itemView.setOnClickListener {
            val dialogFragment = JournalEntryInfoPopUp(currentEntry)
            dialogFragment.setStyle(DialogFragment.STYLE_NORMAL,R.style.settings_custom_popups)
            dialogFragment.show((holder.itemView.context as AppCompatActivity).supportFragmentManager, "journalEntryInfoPopup")
        }
    }
}