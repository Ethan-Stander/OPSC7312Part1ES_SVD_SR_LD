package com.example.opsc7312part1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import kotlin.math.log

class myStoreDetailPopUp : DialogFragment() {

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
        return inflater.inflate(R.layout.fragment_my_store_detail_pop_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val exitButton = view.findViewById<ImageView>(R.id.myStoresPopUpExitButton)
        exitButton.setOnClickListener {
            dismiss()
        }

        val editButton = view.findViewById<ImageView>(R.id.btnEditStore)
        val editMyNotes = view.findViewById<EditText>(R.id.myNotes_info)

        editButton.setOnClickListener {
            editMyNotes.isEnabled = !editMyNotes.isEnabled
        }

    }
}