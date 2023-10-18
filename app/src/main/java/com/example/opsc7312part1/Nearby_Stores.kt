package com.example.opsc7312part1

import PlacesAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Nearby_Stores : Fragment() {
private lateinit var btnBack:Button
private lateinit var recyclerView : RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nearby__stores, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rNearbyplaces)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        btnBack = view.findViewById(R.id.btnNearbyStoresBack)

        val adapter = PlacesAdapter(activity as FragmentNavigation)

        Log.e("HIcount", adapter.itemCount.toString() )
        recyclerView.adapter = adapter

        btnBack.setOnClickListener{
            (requireActivity() as FragmentTesting).replaceFragment(map_nearby(),"Where to Shop")
        }

    }
}
