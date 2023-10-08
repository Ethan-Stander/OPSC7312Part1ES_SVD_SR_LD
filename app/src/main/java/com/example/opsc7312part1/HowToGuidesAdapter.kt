package com.example.opsc7312part1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.compose.ui.layout.Layout
import androidx.recyclerview.widget.RecyclerView

class HowToGuidesAdapter(private val guideList: List<HowToGuidesInfo>) :
    RecyclerView.Adapter<HowToGuidesAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    class ViewHolder(ItemView: View, listener: onItemClickListener) :
        RecyclerView.ViewHolder(ItemView) {
        val title: TextView = itemView.findViewById(R.id.guideTitle)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HowToGuidesAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.how_to_guides_item_view, parent, false)
        return ViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: HowToGuidesAdapter.ViewHolder, position: Int) {
        val currentGuide = guideList[position]
        holder.title.text = currentGuide.title
        holder.itemView.setOnClickListener {
            mListener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return guideList.size
    }
}