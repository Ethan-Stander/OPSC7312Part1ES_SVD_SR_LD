package com.example.opsc7312part1

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.opsc7312part1.databinding.ActivityFragmentTestingBinding

class FragmentTesting :AppCompatActivity() {

    private lateinit var binding: ActivityFragmentTestingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        //implement binding
        binding = ActivityFragmentTestingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.SensorDataFragment.setOnClickListener {
            replaceFragment(Sensor_data_fragment())
        }

        binding.SensorAdjustmentFragment.setOnClickListener {
            replaceFragment(Sensor_adjustment_fragment())
        }

    }

    //replaces fragment based on clicked button
    private fun replaceFragment(fragment:Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainer,fragment)
        fragmentTransaction.commit()


    }


}