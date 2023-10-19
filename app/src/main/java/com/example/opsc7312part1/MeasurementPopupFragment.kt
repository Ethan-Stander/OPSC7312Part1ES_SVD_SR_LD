package com.example.opsc7312part1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.opsc7312part1.databinding.FragmentMeasurementPopupBinding
import kotlinx.coroutines.launch

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


        if(UserID == "")
        {
            binding.txtMeasurementPopUpError.visibility = View.VISIBLE
            binding.textInputLayoutUnitOfDistance.visibility = View.GONE
//            binding.textInputLayoutTemp.visibility = View.GONE
            binding.textInputLayoutStoreRange.visibility = View.GONE
            binding.btnMeasurementSave.visibility = View.GONE

        }
        GetMeasurementSettings()

        //Temperature dropdown
        val temperatures = resources.getStringArray(R.array.Temperatures)
        val arrayAdapter1 = ArrayAdapter(requireContext(), R.layout.dropdown_item, temperatures)
//        binding.tvTemperature.setAdapter((arrayAdapter1))

        //Unit of Distance dropdown
        val unitOfDistance = resources.getStringArray(R.array.UnitOfDistance)
        val arrayAdapter2 = ArrayAdapter(requireContext(), R.layout.dropdown_item, unitOfDistance)
        binding.tvUnitOfDistance.setAdapter((arrayAdapter2))

        // close popup
        binding.btnMeasurementsClose.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        }

        // Save button
        binding.btnMeasurementSave.setOnClickListener {
            WriteMeasurementSettings()
        }

        return binding.root
    }

    private fun WriteMeasurementSettings(){
        // user object
        val user = User(
            UserID = UserID,
            Username = UserName
        )

        lifecycleScope.launch {
            val retrievedSetting = FirebaseUtils.Get(user)

            retrievedSetting?.let { settings ->
                // Update the shared settings object
//                val selectedTemperature = binding.tvTemperature.text.toString()
                val selectedUnitOfDistance = binding.tvUnitOfDistance.text.toString()
                val maxStoreRange = binding.tvMaxStoreRange.text.toString().toFloatOrNull() ?: 0f

//                settings.Celsius = selectedTemperature == "Celsius (°C)"
                settings.KM = selectedUnitOfDistance == "Kilometers (km)"
                settings.MaxDistance = maxStoreRange

                // Save the updated settings back to Firebase
                val result = FirebaseUtils.updateSettingForUser(user, settings)
                if (result) {
                    Toast.makeText(requireContext(), "Settings saved successfully.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Failed to save settings.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun GetMeasurementSettings(){
        // User object
        val user = User(
            UserID = UserID,
            Username = UserName)

        // Retrieve the user's settings from Firebase using FirebaseUtils.Get
        lifecycleScope.launch {
            val setting = FirebaseUtils.Get(user)

            // Set the values of switches and dropdown based on retrieved settings
            setting?.let {
//                binding.tvTemperature.setText(if (it.Celsius) "Celsius (°C)" else "Fahrenheit (°F)", false)
                binding.tvUnitOfDistance.setText(if (it.KM) "Kilometers (km)" else "Miles (mi.)", false)
                binding.tvMaxStoreRange.setText(it.MaxDistance.toString())
            }
        }
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
