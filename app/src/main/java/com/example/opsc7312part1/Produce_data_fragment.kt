package com.example.opsc7312part1

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Produce_data_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class Produce_data_fragment : Fragment(R.layout.produce_data_fragment) {

    private lateinit var produceDataAdapter: ProduceDataAdapter
    private lateinit var produceDataRecyclerView: RecyclerView

    private lateinit var produceDataList: ArrayList<ProduceData>
    lateinit var produceTitle : Array<String>
    lateinit var produceImage : Array<Int>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        produceDataInitialize()

        val gridlayoutManager = GridLayoutManager(context,2)
        produceDataRecyclerView = view.findViewById(R.id.ProduceDataRecyclerView)
        produceDataRecyclerView.layoutManager = gridlayoutManager
        produceDataAdapter = ProduceDataAdapter(produceDataList)
        produceDataRecyclerView.adapter = produceDataAdapter

        produceDataAdapter.setOnItemClickListener(object : ProduceDataAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val produceTitle = produceTitle[position]
                // Create a new instance of Sensor_adjustment_fragment with the selected data
                val produceSensorDataFragment = Sensor_data_fragment()
                // Replace the current fragment with Sensor_adjustment_fragment
                replaceFragment(Sensor_data_fragment())
                (activity as? FragmentTesting)?.title = "$produceTitle"
            }
        })
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
        return inflater.inflate(R.layout.produce_data_fragment, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Produce_data_fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Produce_data_fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun produceDataInitialize()
    {
        //hardcoded values
        produceDataList = arrayListOf<ProduceData>()

        produceTitle = arrayOf(
            getString(R.string.title_1),
            getString(R.string.title_2)
        )

        produceImage = arrayOf(
            R.drawable.tomato,
            R.drawable.spinach
        )


        for(i in produceTitle.indices){
            val produceData = ProduceData(produceTitle[i], produceImage[i])
            produceDataList.add(produceData)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = parentFragmentManager // Use parentFragmentManager for fragments
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.addToBackStack(null) // Add the transaction to the back stack
        fragmentTransaction.commit()
    }
}