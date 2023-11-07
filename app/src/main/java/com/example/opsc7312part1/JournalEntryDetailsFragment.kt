package com.example.opsc7312part1

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class JournalEntryDetailsFragment : DialogFragment() {


    private lateinit var btnAddPhoto : Button
    private lateinit var journalPhoto : ImageView
    private lateinit var btnSelectDate : Button

    //for datepicker
    private val calendar = Calendar.getInstance()
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
        return inflater.inflate(R.layout.fragment_journal_entry_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val exitButton = view.findViewById<ImageView>(R.id.journalCreateExitButton)
        exitButton.setOnClickListener {
            dismiss()
        }

        btnAddPhoto = view.findViewById(R.id.btnTakePhoto)
        journalPhoto = view.findViewById(R.id.journalTakenPhoto)
        btnSelectDate = view.findViewById(R.id.btnSelectDate)

        btnAddPhoto.setOnClickListener {
            takePhoto()
        }

        btnSelectDate.setOnClickListener {
            selectDate()
        }
    }

    //lets user pick the date of the journal entry
    private fun selectDate() {
        val datePickerDialog = DatePickerDialog(
            requireContext(), R.style.CustomDatePickerDialog, {DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                btnSelectDate.text = "Selected Date: $formattedDate"
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        // Show the DatePicker dialog
        datePickerDialog.show()
    }

    //lets user take photo and show on app
    private fun takePhoto(){
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, pic_id)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pic_id) {
            val photo = data!!.extras!!["data"] as Bitmap?
            journalPhoto.setImageBitmap(photo)
        }
    }
    // Define the variable pic_id which is the request-id of the clicked image.
    companion object {
        private const val pic_id = 123
    }

}