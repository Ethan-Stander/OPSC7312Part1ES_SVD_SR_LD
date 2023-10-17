package com.example.opsc7312part1

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class HowToGuidesFragment : Fragment() {

    private lateinit var guideDataAdapter: HowToGuidesAdapter
    private lateinit var guideRecyclerView: RecyclerView
    private lateinit var guideList: ArrayList<HowToGuidesInfo>

    //for pop up info
    private lateinit var popupWindow: PopupWindow
    private lateinit var popupView: View

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
        return inflater.inflate(R.layout.how_to_guides_fragment, container, false)
    }

    private fun guideInfoInitialize() {
        val howToGuidesTitles = resources.getStringArray(R.array.how_to_guides_title)

        guideList = ArrayList()
        for (title in howToGuidesTitles) {
            guideList.add(HowToGuidesInfo(title)) // Replace "Description" with actual descriptions
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        guideInfoInitialize()
        val layoutManager = LinearLayoutManager(requireActivity())
        guideRecyclerView = view.findViewById(R.id.howToGuidesRecyclerView)
        guideRecyclerView.layoutManager = layoutManager
        guideRecyclerView.setHasFixedSize(true)
        guideDataAdapter = HowToGuidesAdapter(guideList)

        guideDataAdapter.setOnItemClickListener(object : HowToGuidesAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                val currentGuide = guideList[position]
                val guideType = currentGuide.title

                val guideLayoutType = when (guideType) {
                    "Reset system" -> R.layout.reset_system_guide
                    "Manually control equipment" -> R.layout.manually_control_equipment_guide
                    "Connect to SmartHydro" -> R.layout.connect_to_smarthydro_guide
                    else -> R.layout.guide_info_pop_ups
                }
                popupView = LayoutInflater.from(requireActivity())
                    .inflate(R.layout.guide_info_pop_ups, null)
                popupView.findViewById<TextView>(R.id.guide_title).text = currentGuide.title

                val contentContainer: FrameLayout = popupView.findViewById(R.id.content_container)
                val contentLayout =
                    LayoutInflater.from(requireActivity()).inflate(guideLayoutType, null)
                contentContainer.addView(contentLayout)
                popupWindow = PopupWindow(
                    popupView,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    true
                )
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

                //exit button on pop up
                val exitButton: ImageButton = popupView.findViewById(R.id.exitButton)
                exitButton.setOnClickListener {
                    popupWindow.dismiss()
                }
            }


        })


        guideRecyclerView.adapter = guideDataAdapter
    }


}