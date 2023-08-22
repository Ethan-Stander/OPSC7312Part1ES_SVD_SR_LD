package com.example.opsc7312part1

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.DialogFragment
import com.example.opsc7312part1.databinding.FragmentMeasurementPopupBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MeasurementPopupFragment : DialogFragment() {

    private var _binding: FragmentMeasurementPopupBinding? = null
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
        _binding = FragmentMeasurementPopupBinding.inflate(inflater, container, false)

        //Temperature dropdown
        val temperatures = resources.getStringArray(R.array.Temperatures)
        val arrayAdapter1 = ArrayAdapter(requireContext(), R.layout.dropdown_item, temperatures)
        binding.tvTemperature.setAdapter((arrayAdapter1))

        //Unit of Distance dropdown
        val unitOfDistance = resources.getStringArray(R.array.UnitOfDistance)
        val arrayAdapter2 = ArrayAdapter(requireContext(), R.layout.dropdown_item, unitOfDistance)
        binding.tvUnitOfDistance.setAdapter((arrayAdapter2))

        //close popup
        binding.btnMeasurementsClose.setOnClickListener {
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
         * @return A new instance of fragment MeasurementPopupFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MeasurementPopupFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
//Disable edit text:
// binding.tvAutoComplete.setKeyListener(null);
