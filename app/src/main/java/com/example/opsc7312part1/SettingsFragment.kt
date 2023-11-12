package com.example.opsc7312part1


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class SettingsFragment : Fragment() {

    private lateinit var settings_account : Button
    private lateinit var settings_measurements : Button
    private lateinit var settings_appsettings : Button
    private lateinit var settings_setfarm : Button

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

    @SuppressLint("SuspiciousIndentation")
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

        settings_setfarm = view.findViewById(R.id.settings_set_farm)

        settings_setfarm.setOnClickListener()
        {

            val dbhelpher = DatabaseHelper(requireContext())

            if(dbhelpher.getFarmCount() == 0)

            {
                val showPopUp = SettingsSetFarmPopupFragment()
                showPopUp.setStyle(DialogFragment.STYLE_NORMAL,R.style.settings_custom_popups)
                showPopUp.show((activity as AppCompatActivity).supportFragmentManager, "showPopup")
            }

            else

            {
                var helper = DatabaseHelper(requireContext())
                var localRecord = helper.getFirstFarmRecord()
                var temp = null
                var firebaseRecord: FarmClass? = null
                var bCheck : Boolean = false

                lifecycleScope.launch {
                    if (localRecord != null) {
                      firebaseRecord =  FarmClass.getFarmRecordFromFirebase(localRecord.FarmID)

                        if(firebaseRecord != null && localRecord != null)
                            {
                                if (firebaseRecord!!.FarmName != localRecord.FarmName)
                                {
                                    helper.updateFarmRecord(localRecord.FarmID, firebaseRecord!!)
                                    bCheck = true;
                                }
                            }

                    }
                    if(!bCheck)
                    {
                        Toast.makeText(requireContext(), "Your farm has been added, please contact an admin to change it!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(requireContext(), "Your farm has been updated!", Toast.LENGTH_SHORT).show();
                    }
                }



            }




        }

    }


}