package com.example.opsc7312part1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch


class JournalEntryInfoPopUp(journalEntry : Journal) : DialogFragment() {

    private lateinit var txtInfoTitle : TextView
    private lateinit var txtInfoDate : TextView
    private lateinit var txtInfoDesc : TextView
    private lateinit var infoImage : ImageView
    private lateinit var btnDelete : Button
    private lateinit var btnClose : ImageView

    var entry = journalEntry

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
        return inflater.inflate(R.layout.fragment_journal_entry_info_pop_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtInfoTitle = view.findViewById(R.id.info_Title)
        txtInfoDate = view.findViewById(R.id.info_date)
        txtInfoDesc = view.findViewById(R.id.info_description)
        infoImage = view.findViewById(R.id.info_photo)
        btnDelete = view.findViewById(R.id.btnDeleteEntry)
        btnClose = view.findViewById(R.id.infoExitButton)


        btnClose.setOnClickListener {
            dismiss()
        }

        txtInfoTitle.text = entry.title
        txtInfoDate.text = "Date ${entry.date}"
        txtInfoDesc.text = entry.notes
        Picasso.get().load(Uri.parse(entry.imageLink)).into(infoImage)

        val user = User(
            UserID = UserID,
            Username = UserName
        )

        btnDelete.setOnClickListener {


            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Delete Entry")
            builder.setMessage("Are you sure you want to delete this entry?")

            builder.setPositiveButton("Yes") { dialog, _ ->
                lifecycleScope.launch {

                    val isDeleted = JournalUtils.deleteJournalEntry(user, entry.id)

                    if (isDeleted) {
                        dismiss()
                        FragmentUtils.refreshFragment(requireActivity(), JournalFragment(), R.id.frameLayout)
                        Toast.makeText(requireContext(), "Journal entry deleted", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireActivity(), "Failed to delete entry", Toast.LENGTH_SHORT).show()
                    }
                }

                dialog.dismiss()
            }

            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }
    }




}