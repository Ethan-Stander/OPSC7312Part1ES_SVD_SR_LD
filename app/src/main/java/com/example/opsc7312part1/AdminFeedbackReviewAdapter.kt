package com.example.opsc7312part1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView

class AdminFeedbackReviewAdapter(private var feedbackList : List<FeedbackData>):RecyclerView.Adapter<AdminFeedbackReviewAdapter.ViewHolder>() {

    fun setFilteredist(feedbackList: List<FeedbackData>){
        this.feedbackList = feedbackList
        notifyDataSetChanged()
    }
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val feedbackTitle : TextView = itemView.findViewById(R.id.txtfeedbackTitle)
        val feedbackTitleBody : TextView = itemView.findViewById(R.id.txtfeedbackTitleBody)
        val feedbackSender : TextView = itemView.findViewById(R.id.txtfeedbackSender)
        val feedbackSenderBody : TextView = itemView.findViewById(R.id.txtfeedbackSenderBody)
        val feedbackView : CardView = itemView.findViewById(R.id.feedback_items_list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.feedback_list_item,parent,false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return  feedbackList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentFeedback = feedbackList[position]
        holder.feedbackTitle.text = "Title"
        holder.feedbackTitleBody.text = " ・ ${currentFeedback.title}"
        holder.feedbackSender.text = "From"
        holder.feedbackSenderBody.text = " ・ ${currentFeedback.email}"

        holder.feedbackView.setOnClickListener {
            val dialogFragment = FeedbackDetailsPopUp(currentFeedback)
            dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.settings_custom_popups)
            dialogFragment.show((holder.itemView.context as AppCompatActivity).supportFragmentManager, "FeedbackDetailsPopUp")
        }
    }
}