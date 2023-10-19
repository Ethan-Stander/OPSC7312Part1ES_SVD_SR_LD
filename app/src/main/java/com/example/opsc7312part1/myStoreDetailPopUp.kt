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
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.opsc7312part1.MyStore.Companion.updateStoreWithPlaceId
import com.example.opsc7312part1.MyStore.Companion.updateStoreWithPlaceIdFav
import kotlinx.coroutines.launch
import kotlin.math.log

class myStoreDetailPopUp(myStore: MyStore) : DialogFragment() {

    var myStore = myStore

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
        val editName = view.findViewById<TextView>(R.id.storeTitle)
        val description = view.findViewById<TextView>(R.id.storeDetails_description_info)
        editMyNotes.setText(myStore.notes)
        editName.text = myStore.name
        var isOpen = "";
        if(myStore.isOpenNow)
        {
            isOpen = "Open"
        }
        else
        {
            isOpen = "Closed"
        }
        description.text = myStore.address + "\n" + myStore.rating + "\n" + "\n" + isOpen


        editButton.setOnClickListener {
            editMyNotes.isEnabled = !editMyNotes.isEnabled

            if(!editMyNotes.text.toString().equals(myStore.notes))
            {
                myStore.notes = editMyNotes.text.toString()
                lifecycleScope.launch {
                    UserID?.let { updateStoreWithPlaceId(it,myStore.placeId,myStore) }
                }


            }
        }




    }
}