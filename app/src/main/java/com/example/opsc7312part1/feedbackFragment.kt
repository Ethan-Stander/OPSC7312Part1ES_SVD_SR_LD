package com.example.opsc7312part1

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.util.Patterns
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
    private lateinit var edtSender : EditText
    private lateinit var btnSend : Button
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
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
        edtSender = view.findViewById(R.id.edtEmailSender)
        btnSend = view.findViewById(R.id.btnSendFeedback)






        btnSend.setOnClickListener {
            if(edtSubject.text.isNullOrEmpty() || edtBody.text.isNullOrEmpty() || edtSender.text.isNullOrEmpty())
            {
                Toast.makeText(requireActivity(),"Please make sure all information is entered before sending any feedback",Toast.LENGTH_SHORT).show()
            }else
            {
                if(!isValidEmail(edtSender.text.toString().trim()))
                {
                    Toast.makeText(requireActivity(),"Please make sure email is correct",Toast.LENGTH_SHORT).show()
                }else
                {
                    var title = edtSubject.text.toString().trim()
                    var emailSender = edtSender.text.toString().trim()
                    var body = edtBody.text.toString().trim()

                    val feedback = FeedbackData(title,emailSender,body)
                    FeedbackUtil.sendFeedbackToFirebase(listOf(feedback))

                    Toast.makeText(requireActivity(), "Feedback sent successfully", Toast.LENGTH_SHORT).show()

                    edtSubject.text.clear()
                    edtBody.text.clear()
                    edtSender.text.clear()

                }
            }
        }
    }

    //check is email input is in email format
    fun isValidEmail(email: String): Boolean {
        return email.matches(emailRegex.toRegex())
    }
}