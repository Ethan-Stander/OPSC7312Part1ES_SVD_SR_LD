package com.example.opsc7312part1


import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class SettingsFragment : Fragment() {

    private lateinit var settings_account : Button
    private lateinit var settings_measurements : Button
    private lateinit var settings_appsettings : Button

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
        return inflater.inflate(R.layout.fragment_settings, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Popups

        settings_account = view.findViewById(R.id.settings_account)
        settings_account.setOnClickListener {
            //custom popup
            val showPopup = AccountPopupFragment()

            showPopup.setStyle(DialogFragment.STYLE_NORMAL, R.style.settings_custom_popups)

            showPopup.show((activity as AppCompatActivity).supportFragmentManager, "showPopup")
        }

        settings_measurements = view.findViewById(R.id.settings_measurements)
        settings_measurements.setOnClickListener {
            //custom popup
            val showPopup = MeasurementPopupFragment()

            showPopup.setStyle(DialogFragment.STYLE_NORMAL, R.style.settings_custom_popups)

            showPopup.show((activity as AppCompatActivity).supportFragmentManager, "showPopup")
        }

        settings_appsettings = view.findViewById(R.id.settings_appsettings)
        settings_appsettings.setOnClickListener {
            //custom popup
            val showPopup = AppSettingsPopupFragment()

            showPopup.setStyle(DialogFragment.STYLE_NORMAL, R.style.settings_custom_popups)

            showPopup.show((activity as AppCompatActivity).supportFragmentManager, "showPopup")
        }
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

//default popups
//    private fun showPopupDialog(title: String, message: String) {
//        val alertDialogBuilder = AlertDialog.Builder(requireContext())
//
//        alertDialogBuilder.setTitle(title)
//        alertDialogBuilder.setMessage(message)
//
//        alertDialogBuilder.setPositiveButton("OK") { dialog, which ->
//            dialog.dismiss() // Dismiss the dialog when the "OK" button is clicked
//        }
//
//        val alertDialog = alertDialogBuilder.create()
//        alertDialog.show()
//    }