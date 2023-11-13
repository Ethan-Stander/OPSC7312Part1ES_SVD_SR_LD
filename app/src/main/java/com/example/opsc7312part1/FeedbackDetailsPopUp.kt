package com.example.opsc7312part1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class FeedbackDetailsPopUp(feedback: FeedbackData) : DialogFragment() {

    var feedback = feedback
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feedback_details_pop_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val exitButton = view.findViewById<ImageView>(R.id.feedbackPopUpExitButton)
        exitButton.setOnClickListener {
            dismiss()
        }

        val title = view.findViewById<TextView>(R.id.feedbackTitle)
        val sender = view.findViewById<TextView>(R.id.feedbackSender)
        val body = view.findViewById<TextView>(R.id.feedbackBody)
        val btnDelete = view.findViewById<Button>(R.id.btnDeleteFeedback)

        title.text = feedback.title
        sender.text = " ・ ${feedback.email}"
        body.text = feedback.body

        btnDelete.setOnClickListener {
            lifecycleScope.launch {
                   if( FeedbackUtil.deleteFeedbackById(feedback.id))
                   {
                       FragmentUtils.refreshFragment(requireActivity(), AdminFeedbackReview_fragement(), R.id.frameLayout)
                       dismiss()
                   }
            }
        }
    }
}