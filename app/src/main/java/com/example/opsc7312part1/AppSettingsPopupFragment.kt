package com.example.opsc7312part1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import androidx.fragment.app.DialogFragment
import com.example.opsc7312part1.databinding.FragmentAppSettingsPopupBinding
import com.example.opsc7312part1.databinding.FragmentMeasurementPopupBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AppSettingsPopupFragment : DialogFragment() {

    private var _binding: FragmentAppSettingsPopupBinding? = null
    private val binding get() = _binding!!

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
        _binding = FragmentAppSettingsPopupBinding.inflate(inflater, container, false)

        //App Theme dropdown
        val appTheme = resources.getStringArray(R.array.AppTheme)
        val arrayAdapter1 = ArrayAdapter(requireContext(), R.layout.dropdown_item, appTheme)
        binding.tvAppTheme.setAdapter((arrayAdapter1))

        //close popup
        binding.btnAppSettingsClose.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AppSettingsPopupFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AppSettingsPopupFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}