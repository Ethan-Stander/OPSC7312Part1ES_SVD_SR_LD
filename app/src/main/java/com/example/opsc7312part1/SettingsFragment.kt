package com.example.opsc7312part1


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SettingsFragment : Fragment() {

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
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        val settingsAccountButton = view.findViewById<Button>(R.id.settings_account)
        settingsAccountButton.setOnClickListener {
            showPopupDialog("Account", "This is the account settings.")
        }

        val settingsMeasurementsButton = view.findViewById<Button>(R.id.settings_measurements)
        settingsMeasurementsButton.setOnClickListener {
            showPopupDialog("Measurements", "This is the measurements settings.")
        }

        val settingsAppSettingsButton = view.findViewById<Button>(R.id.settings_appsettings)
        settingsAppSettingsButton.setOnClickListener {
            showPopupDialog("App Settings", "This is the app settings.")
        }

        return view

    }
    private fun showPopupDialog(title: String, message: String) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())

        alertDialogBuilder.setTitle(title)
        alertDialogBuilder.setMessage(message)

        alertDialogBuilder.setPositiveButton("OK") { dialog, which ->
            dialog.dismiss() // Dismiss the dialog when the "OK" button is clicked
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}