package com.example.opsc7312part1

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.opsc7312part1.databinding.FragmentAppSettingsPopupBinding
import com.example.opsc7312part1.databinding.FragmentMeasurementPopupBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AppSettingsPopupFragment : DialogFragment() {

    private var _binding: FragmentAppSettingsPopupBinding? = null
    private  var switchNotifications : Boolean = true

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

        checkNotificationPermission()
       /* GetAppSettings()*/

        // App Theme dropdown
        val appTheme = resources.getStringArray(R.array.AppTheme)
        val arrayAdapter1 = ArrayAdapter(requireContext(), R.layout.dropdown_item, appTheme)
        binding.tvAppTheme.setAdapter((arrayAdapter1))

        // Load the saved theme preference and apply it
        val sharedPreferences = requireActivity().getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val savedTheme = sharedPreferences.getString("theme", "Light Mode")

        // Set the initial selection based on the saved preference
        binding.tvAppTheme.setText(savedTheme, false)

        binding.tvAppTheme.setOnItemClickListener { _, _, position, _ ->
            val selectedTheme = appTheme[position]
            // Save the selected theme to SharedPreferences
            sharedPreferences.edit().putString("theme", selectedTheme).apply()
            applyTheme(selectedTheme)
        }

        // close popup
        binding.btnAppSettingsClose.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        }

        binding.switchNotifications.setOnClickListener {
            notificationPermission()
        }


        /*// Save Button
        binding.btnAppSettingSave.setOnClickListener{
                WriteAppSettings()
        }*/

        return binding.root
    }

    private fun applyTheme(selectedTheme: String) {
        requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        val themeId = if (selectedTheme == "Light Mode") R.style.AppTheme_Light else R.style.AppTheme_Dark
        requireActivity().setTheme(themeId)
        requireActivity().recreate() // Recreate the activity to apply the new theme
    }

    private fun checkNotificationPermission() {
        binding.switchNotifications.isChecked =
            ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    }
    private fun notificationPermission(){
        val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri: Uri = Uri.fromParts("package", requireActivity().packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    fun WriteAppSettings(){
        // user object
        val user = User(
            UserID = UserID,
            Username = UserName
        )

        lifecycleScope.launch {
            val retrievedSetting = FirebaseUtils.Get(user)

            retrievedSetting?.let { settings ->
                // Update the shared settings object
                val selectedTheme = binding.tvAppTheme.text.toString()
                switchNotifications = binding.switchNotifications.isChecked
                val switchLocation = binding.switchLocation.isChecked
                if(switchNotifications)
                {
                    notificationPermission()
                }
                settings.LightTheme = selectedTheme == "Light Mode"
                settings.Notifications = switchNotifications
                settings.LocationPermission = switchLocation

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

//    val selectedTheme = binding.tvAppTheme.text.toString()
//    val switchNotifications = binding.switchNotifications.isChecked
//    val switchLocation = binding.switchLocation.isChecked
//
//    // User object
//    val user = User(
//        UserID = UserID,
//        Username = UserName)
//
//    // Setting object
//    val setting = Setting(
//        LightTheme = selectedTheme == "Light Mode",
//        Notifications = switchNotifications,
//        LocationPermission = switchLocation)
//
//    // insertSettingForUser method within a coroutine
//    lifecycleScope.launch {
//        val result = FirebaseUtils.updateSettingForUser(user, setting)
//        if (result) {
//            Toast.makeText(requireContext(), "Settings saved successfully.", Toast.LENGTH_SHORT).show()
//        } else {
//            Toast.makeText(requireContext(), "Failed to save settings.", Toast.LENGTH_SHORT).show()
//        }
//    }
    fun GetAppSettings(){
        // User object
        val user = User(
            UserID = UserID,
            Username = UserName)

        // Retrieve the user's settings from Firebase using FirebaseUtils.Get
        lifecycleScope.launch {
            val setting = FirebaseUtils.Get(user)

            // Set the values of switches and dropdown based on retrieved settings
            setting?.let {
                binding.switchNotifications.isChecked = it.Notifications
                binding.switchLocation.isChecked = it.LocationPermission
                binding.tvAppTheme.setText(if (it.LightTheme) "Light Mode" else "Dark Mode", false)
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