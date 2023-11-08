package com.example.opsc7312part1

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class JournalEntryAdapter(private var journalEntryList: List<Journal>):RecyclerView.Adapter<JournalEntryAdapter.ViewHolder>() {
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val entryDetails : TextView = itemView.findViewById(R.id.txtJournalDetails)
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
        holder.entryDetails.text = "${currentEntry.title}\nDate: ${currentEntry.date}"
        holder.entryImage.setImageURI(Uri.parse(currentEntry.imageLink))
        Picasso.get().load(Uri.parse(currentEntry.imageLink)).into(holder.entryImage)
    }
}