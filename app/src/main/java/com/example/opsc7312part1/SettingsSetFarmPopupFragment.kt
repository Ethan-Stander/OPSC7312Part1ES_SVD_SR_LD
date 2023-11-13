package com.example.opsc7312part1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.opsc7312part1.databinding.FragmentMeasurementPopupBinding
import com.example.opsc7312part1.databinding.FragmentSetFarmPopupBinding
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsSetFarmPopupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsSetFarmPopupFragment : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var _binding: FragmentSetFarmPopupBinding? = null
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

        _binding = FragmentSetFarmPopupBinding.inflate(inflater, container, false)

        _binding!!.btnFarmClose.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        }

        val myFarmtext = _binding!!.edtFarm;

        _binding!!.btnFarmSave.setOnClickListener {

            var db = DatabaseHelper(requireContext())

            if(db.addFarm(myFarmtext.text.toString()))
            {
                val builder = AlertDialog.Builder(requireContext(), R.style.confirmation_popups)
                builder.setTitle("Set Farm")
                builder.setMessage("Are you sure you want to name your farm this?")

                builder.setPositiveButton("Yes") { dialog, _ ->
                    lifecycleScope.launch {

                        var farm = db.getFirstFarmRecord()
                        if (farm != null) {
                            FarmClass.addFarmRecordToFirebase(farm)
                        }
                        Toast.makeText(requireContext(), "Your farm has been added", Toast.LENGTH_SHORT).show();
                        requireActivity().supportFragmentManager.beginTransaction().remove(this@SettingsSetFarmPopupFragment).commit()
                    }
                    dialog.dismiss()
                }
                builder.setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                val dialog = builder.create()
                dialog.show()
            }

        }
        return binding.root




    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingsSetFarmPopupFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingsSetFarmPopupFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}