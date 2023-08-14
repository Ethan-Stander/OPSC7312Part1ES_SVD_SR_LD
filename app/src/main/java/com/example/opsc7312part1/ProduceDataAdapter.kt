package com.example.opsc7312part1

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.ui.layout.Layout
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso



class ProduceDataAdapter(private val produceDataList :ArrayList<ProduceData>) : RecyclerView.Adapter<ProduceDataAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    class ViewHolder(itemView:View, listener: onItemClickListener):RecyclerView.ViewHolder(itemView) {
        val produceTitle : TextView = itemView.findViewById(R.id.produceTitle)
        val produceImage : ImageView = itemView.findViewById(R.id.produceImage)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProduceDataAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.produce_grid_item_view,parent,false)
        return ViewHolder(itemView,mListener)
    }

    override fun onBindViewHolder(holder: ProduceDataAdapter.ViewHolder, position: Int) {
        val currentItem = produceDataList[position]
        holder.produceTitle.text = currentItem.produceTitle
        Log.i("adapter images","url : ${currentItem.produceImage}")
        Picasso.get().load(currentItem.produceImage).into(holder.produceImage)
    }

    override fun getItemCount(): Int {
        //replace with produceDataList
        return produceDataList.size
    }
}