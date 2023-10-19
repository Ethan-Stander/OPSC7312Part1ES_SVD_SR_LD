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


    //lists for dummy data
    lateinit var myStoresNames: Array<String>
    lateinit var myStoresInfo: Array<String>
    lateinit var myStoresFavorite: Array<Boolean>
    lateinit var myStoresRating: Array<String>


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter_stores_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //TODO
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

       lifecycleScope.launch {
           myStoresDetailsList = MyStore.getStoresForUser(user);
           myStoresAdapter = MyStoresAdapter(myStoresDetailsList)
           myStoresRecyclerView.adapter = myStoresAdapter
       }
    }

private fun filterList(query: String?) {
    if (query != null){

        var myStores = (mutableListOf <MyStore>())
        for(i in myStoresDetailsList){
            if(i.name.lowercase(Locale.ROOT).contains(query)){
                myStores.add(i)
            }
        }

        myStoresAdapter.setFilteredist(myStores)
    }
}

}