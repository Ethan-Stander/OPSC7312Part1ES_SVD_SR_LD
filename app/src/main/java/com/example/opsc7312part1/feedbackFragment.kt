package com.example.opsc7312part1

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast


class feedbackFragment : Fragment() {

    private lateinit var edtSubject : EditText
    private lateinit var edtBody : EditText
    private lateinit var btnSend : Button
    private val sendTo = "st10085288@vcconnect.edu.za"
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
        return inflater.inflate(R.layout.feedback_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        edtSubject = view.findViewById(R.id.edtEmailTitle)
        edtBody = view.findViewById(R.id.edtEmailBody)
        btnSend = view.findViewById(R.id.btnSendFeedback)

        btnSend.setOnClickListener {
            if(edtSubject.text.isNullOrEmpty() || edtBody.text.isNullOrEmpty() )
            {
                Toast.makeText(requireActivity(),"Please make sure all information is entered before sending any feedback",Toast.LENGTH_SHORT).show()
            }else
            {
                val emailSubject = edtSubject.text.trim().toString()
                val emailBody = edtBody.text.trim().toString()
                val intent = Intent(Intent.ACTION_SEND)
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(sendTo))
                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for SmartHydro: $emailSubject")
                intent.putExtra(Intent.EXTRA_TEXT,emailBody)

                intent.type = "message/rfc822"

                startActivity(Intent.createChooser(intent, "Choose an Email service :"))
                edtSubject.text.clear()
                edtBody.text.clear()
            }
        }
    }
}