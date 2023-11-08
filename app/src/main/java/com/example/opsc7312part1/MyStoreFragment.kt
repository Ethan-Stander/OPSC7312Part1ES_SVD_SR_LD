package com.example.opsc7312part1

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import java.util.Locale
import androidx.appcompat.widget.SearchView

class MyStoreFragment : Fragment() {

    private lateinit var myStoresAdapter: MyStoresAdapter
    private lateinit var myStoresRecyclerView: RecyclerView
    private lateinit var myStoresDetailsList: List<MyStore>
    private lateinit var searchView : SearchView
    private lateinit var tvMyStoreErrorMessage : TextView
    private lateinit var myStoreLoadingBar : ProgressBar

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter_stores_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.store_filter_rating -> {

                val sortedList = myStoresDetailsList.sortedByDescending { it.rating }
                myStoresAdapter = MyStoresAdapter(sortedList)
                myStoresRecyclerView.adapter = myStoresAdapter
                return true
            }

            R.id.store_filter_alphabetical -> {

                val sortedList = myStoresDetailsList.sortedBy { it.name }
                myStoresAdapter = MyStoresAdapter(sortedList)
                myStoresRecyclerView.adapter = myStoresAdapter
                return true
            }

            R.id.store_filter_favorite -> {

                val filteredList = myStoresDetailsList.filter { it.favorite }
                myStoresAdapter = MyStoresAdapter(filteredList)
                myStoresRecyclerView.adapter = myStoresAdapter
                return true

            }

            R.id.default_filter -> {

                myStoresAdapter = MyStoresAdapter(myStoresDetailsList)
                myStoresRecyclerView.adapter = myStoresAdapter

                return true
            }


            else -> return super.onOptionsItemSelected(item)
        }


    }


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

        val rootView = inflater.inflate(R.layout.fragment_my_store, container, false)
        (activity as AppCompatActivity).supportActionBar?.apply {
            setHasOptionsMenu(true)
        }
        myStoresDetailsList = emptyList()

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchView  = view.findViewById<SearchView>(R.id.searchStore)

        myStoresRecyclerView = view.findViewById(R.id.myStoreRecyclerView)

        myStoreLoadingBar = view.findViewById(R.id.myStoreLoadingBar)

        tvMyStoreErrorMessage = view.findViewById(R.id.tvMyStoreErrorMessage)

        searchView .setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })

        //my stores recyclerview
        myStoreDetailsInitialize()
        val LayoutManager = LinearLayoutManager(context)
        myStoresRecyclerView = view.findViewById(R.id.myStoreRecyclerView)
        myStoresRecyclerView.layoutManager = LayoutManager
        myStoresRecyclerView.setHasFixedSize(true)
        myStoresAdapter = MyStoresAdapter(myStoresDetailsList)
        myStoresRecyclerView.adapter = myStoresAdapter

    }

   private fun myStoreDetailsInitialize() {

       val user = User(
           UserID = UserID,
           Username = UserName
       )
       if (UserID == "") {
           showError("No Stores Found...")
           searchView.visibility = View.GONE
       }
       else {
           showLoading()
           lifecycleScope.launch {
               myStoresDetailsList = MyStore.getStoresForUser(user)
               myStoresAdapter = MyStoresAdapter(myStoresDetailsList)
               myStoresRecyclerView.adapter = myStoresAdapter

               hideLoading()
           }
       }

    }

private fun filterList(query: String?) {
    if (query != null){

        var myStores = (mutableListOf <MyStore>())
        for(i in myStoresDetailsList){
            if(i.name.lowercase(Locale.ROOT).contains(query.lowercase())){
                myStores.add(i)
            }
        }

        myStoresAdapter.setFilteredist(myStores)
    }
}

    // show error if stores does not load
    private fun showError(errorMessage: String) {
        tvMyStoreErrorMessage.text = errorMessage
        tvMyStoreErrorMessage.visibility = View.VISIBLE
        myStoresRecyclerView.visibility = View.GONE // Hide the RecyclerView
    }

    // hide if stores shows
    private fun hideError() {
        tvMyStoreErrorMessage.visibility = View.GONE
        myStoresRecyclerView.visibility = View.VISIBLE
    }

    private fun showLoading() {
        myStoreLoadingBar.visibility = View.VISIBLE
        hideError() // Hide the error message
    }

    private fun hideLoading() {
        myStoreLoadingBar.visibility = View.GONE
        myStoresRecyclerView.visibility = View.VISIBLE
    }

}