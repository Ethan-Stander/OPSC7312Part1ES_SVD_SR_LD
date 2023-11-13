package com.example.opsc7312part1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FarmOverViewFragment : Fragment() {

    private lateinit var rVActions: RecyclerView
    private lateinit var rVData: RecyclerView

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
        val view = inflater.inflate(R.layout.fragment_farm_over_view, container, false)

        rVActions = view.findViewById(R.id.rVActions)
        rVData = view.findViewById(R.id.rVData)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val actionsRecyclerView: RecyclerView = view.findViewById(R.id.rVActions)

        // Call the fetchActions method to get data from Firebase
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val actions = ActionUtils.fetchActions()

                // Convert List<Action> to ArrayList<Action>
                val actionsArrayList = ArrayList(actions)

                val eventAdapter = EventAdapter(actionsArrayList, requireContext())
                actionsRecyclerView.adapter = eventAdapter
            } catch (e: Exception) {
                // Handle exception
                e.printStackTrace()
            }
        }
    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            FarmOverViewFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}