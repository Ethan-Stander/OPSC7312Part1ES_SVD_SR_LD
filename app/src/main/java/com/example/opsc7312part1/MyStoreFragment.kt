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

class MyStoreFragment : Fragment() {

    private lateinit var myStoresAdapter: MyStoresAdapter
    private lateinit var myStoresRecyclerView: RecyclerView
    private var myStoresDetailsList : List<MyStore>  = emptyList()


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
        lifecycleScope.launch {
            myStoreDetailsInitialize()
        }

        val LayoutManager = LinearLayoutManager(context)
        myStoresRecyclerView = view.findViewById(R.id.myStoreRecyclerView)
        myStoresRecyclerView.layoutManager = LayoutManager
        myStoresRecyclerView.setHasFixedSize(true)
        myStoresAdapter = MyStoresAdapter(myStoresDetailsList)
        myStoresRecyclerView.adapter = myStoresAdapter

    }

    suspend fun  myStoreDetailsInitialize() {
        val user = User(
            UserID = UserID,
            Username = UserName
        )
           val storelist = MyStore.getStoresForUser(user)

            storelist?.let {
                myStoresDetailsList = storelist
                myStoresAdapter.notifyDataSetChanged()
            }
        }

    }

