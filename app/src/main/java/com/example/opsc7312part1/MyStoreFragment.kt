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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MyStoreFragment : Fragment() {

    private lateinit var myStoresAdapter: MyStoresAdapter
    private lateinit var myStoresRecyclerView: RecyclerView
    private lateinit var myStoresDetailsList : ArrayList<MyStore>

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

                return true
            }
            R.id.store_filter_alphabetical -> {

                return true
            }
            R.id.store_filter_favorite -> {

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
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        myStoresDetailsList = arrayListOf<MyStore>()

        myStoresNames = arrayOf(
            "Builders Warehouse",
            "Jared Forlee Hardware"
        )

        myStoresInfo = arrayOf(
            "Port Elizabeth",
            "Port Elizabeth"
        )
        myStoresFavorite = arrayOf(
            true,
            false
        )
        myStoresRating = arrayOf(
            "4",
            "3"
        )


        for (i in myStoresNames.indices)
        {
            val myStoreData = MyStore(myStoresNames[i],myStoresInfo[i],myStoresFavorite[i],myStoresRating[i].toFloatOrNull(),null,null)
            myStoresDetailsList.add(myStoreData)
        }
    }

}