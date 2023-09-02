package com.example.opsc7312part1

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.WindowCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.opsc7312part1.databinding.ActivityFragmentTestingBinding
import com.google.android.material.navigation.NavigationView
import java.time.LocalTime
import java.util.Calendar
import java.util.Date

class FragmentTesting :AppCompatActivity() {

    private lateinit var binding: ActivityFragmentTestingBinding
    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout

    @SuppressLint("NewApi", "ScheduleExactAlarm")
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#428948")))

        //API background service
        val intent = Intent(this, APICallService::class.java)
        startService(intent)

        //implement binding
        binding = ActivityFragmentTestingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //nav bar
        drawerLayout = findViewById(R.id.SideNavigation)
        val navView : NavigationView = findViewById(R.id.sideNavigationView)

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        replaceFragment(Produce_data_fragment(),"Produce")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
            //highlights item
            it.isChecked = true
            when(it.itemId){
                R.id.nav_produce ->replaceFragment(Produce_data_fragment(),it.title.toString())
                R.id.nav_equipment ->replaceFragment(Equipment_status_fragment(),it.title.toString())
                R.id.nav_settings ->replaceFragment(SettingsFragment(),it.title.toString())
            }
            true
        }
    }

    //disables back button on phone default navigation bar
    //don't remove, its works
    override fun onBackPressed() {
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    //replaces fragment based on clicked nav item and passes title to be displayed
    private fun replaceFragment(fragment:Fragment,title:String) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.commit()
        drawerLayout.closeDrawers()
        setTitle(title)
    }


}