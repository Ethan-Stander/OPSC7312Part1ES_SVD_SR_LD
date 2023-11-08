package com.example.opsc7312part1

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch


class JournalFragment : Fragment() {

    private lateinit var journalEntryAdapter: JournalEntryAdapter
    private lateinit var journalEntryRecyclerView: RecyclerView
    private lateinit var journalEntryList: List<Journal>

    private lateinit var searchView : SearchView
    private lateinit var journalEntriesLoadingBar : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_journal, container, false)
        (activity as AppCompatActivity).supportActionBar?.apply {
            setHasOptionsMenu(true)
        }
        journalEntryList = emptyList()
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchView  = view.findViewById(R.id.searchJournalEntry)
       // myStoreLoadingBar = view.findViewById(R.id.myStoreLoadingBar)


        journalEntriesInitialize()
        val layoutManager = LinearLayoutManager(context)
        journalEntryRecyclerView = view.findViewById(R.id.journalEntriesRecyclerView)
        journalEntryRecyclerView.layoutManager = layoutManager
        journalEntryRecyclerView.setHasFixedSize(true)
        journalEntryAdapter = JournalEntryAdapter(journalEntryList)
        journalEntryRecyclerView.adapter = journalEntryAdapter
    }

    private fun journalEntriesInitialize() {
        val user = User(
            UserID = UserID,
            Username = UserName
        )

        lifecycleScope.launch {
            journalEntryList = JournalUtils.getJournalEntries(user)
            journalEntryAdapter = JournalEntryAdapter(journalEntryList)
            journalEntryRecyclerView.adapter = journalEntryAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.action_bar_journal, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.openJournalEntry -> {
                OpenJournalDetailsPopUp()
                return true
            }


            else -> return super.onOptionsItemSelected(item)
        }


    }



    private fun OpenJournalDetailsPopUp()
    {
        val dialogFragment = JournalEntryDetailsFragment()
        dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.settings_custom_popups)
        dialogFragment.show((activity as AppCompatActivity).supportFragmentManager,"showJournalDetails")
    }

}