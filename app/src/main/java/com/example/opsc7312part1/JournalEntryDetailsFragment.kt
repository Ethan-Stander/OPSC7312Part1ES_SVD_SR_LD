package com.example.opsc7312part1

import android.app.Activity.RESULT_CANCELED
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class JournalEntryDetailsFragment : DialogFragment() {

    private lateinit var btnExit : ImageView
    private lateinit var btnAddPhoto : Button
    private lateinit var journalPhoto : ImageView
    private lateinit var btnSelectDate : Button
    private  lateinit var  btnSaveEntry : Button
    private lateinit var txtTitle : EditText
    private lateinit var  txtNotes : EditText
    private var entryDate : String = ""
    private var photoBitMap : Bitmap? = null
    private var photoUri : Uri? = null
    private var imageLink :String= ""

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

        btnExit = view.findViewById(R.id.journalCreateExitButton)
        btnAddPhoto = view.findViewById(R.id.btnTakePhoto)
        journalPhoto = view.findViewById(R.id.journalTakenPhoto)
        btnSelectDate = view.findViewById(R.id.btnSelectDate)
        btnSaveEntry = view .findViewById(R.id.btnSaveEntry)
        txtTitle = view.findViewById(R.id.edt_journal_create_title)
        txtNotes = view.findViewById(R.id.edt_journal_create_notes)

        btnExit.setOnClickListener {
            dismiss()
        }

        btnAddPhoto.setOnClickListener {
            takePhoto()
        }

        btnSelectDate.setOnClickListener {
            selectDate()
        }

        btnSaveEntry.setOnClickListener {
            JournalUtils.saveJournalImage(photoUri) { photoLink ->
                imageLink = photoLink
                saveEntry()
            }

        }
    }

    private fun saveEntry() {
        //user object
        val user = User(
            UserID = UserID,
            Username = UserName
        )

        //journal object
        val journal = Journal(
            title = txtTitle.text.toString(),
            notes = txtNotes.text.toString(),
            date = entryDate,
            imageLink = imageLink
        )

        lifecycleScope.launch {
            try {
                if (journal.title.isNullOrEmpty() || journal.notes.isNullOrEmpty() || journal.date.isNullOrEmpty() || journal.imageLink.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), "Please ensure all fields are filled in .", Toast.LENGTH_SHORT).show()
                }else
                {
                    val result = JournalUtils.saveJournalEntry(user, journal)
                    if (result) {
                        Toast.makeText(requireContext(), "Entry saved successfully.", Toast.LENGTH_SHORT).show()
                        dismiss()
                    } else {
                        Toast.makeText(requireContext(), "Failed to save entry.", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
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
                entryDate =  formattedDate.toString()
                btnSelectDate.text = entryDate
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
        if(resultCode != RESULT_CANCELED) {
            if (requestCode == pic_id) {

                photoBitMap = data!!.extras!!["data"] as Bitmap?

                //converts bitmap to uri
                val bytes = ByteArrayOutputStream()
                photoBitMap?.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                val path: String = MediaStore.Images.Media.insertImage(
                    requireContext().getContentResolver(),
                    photoBitMap,
                    "Title",
                    null
                )
                photoUri =  Uri.parse(path)

                if (photoUri != null) {
                    journalPhoto.setImageURI(photoUri)
                    Log.i("Check phot uri:", photoUri.toString())
                }
            }
        }

    }
    // Define the variable pic_id which is the request-id of the clicked image.
    companion object {
        private const val pic_id = 123
    }

}