package com.example.opsc7312part1

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ClipData.Item
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.widget.Group
import androidx.core.app.NotificationCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.opsc7312part1.databinding.ActivityFragmentTestingBinding
import com.example.opsc7312part1.ui.theme.OPSC7312Part1Theme
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.Calendar
import java.util.Date
import kotlin.concurrent.thread

class FragmentTesting :AppCompatActivity(), FragmentNavigation {

    private lateinit var binding: ActivityFragmentTestingBinding
    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var adminGroup: MenuItem;

    companion object {
        public lateinit var fragmentNavigation: FragmentNavigation
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#428948")))

        // Set the theme based on the saved preference
        val sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val savedTheme = sharedPreferences.getString("theme", "Light Mode")
        val themeId = if (savedTheme == "Light Mode") AppCompatDelegate.MODE_NIGHT_NO else AppCompatDelegate.MODE_NIGHT_YES
        AppCompatDelegate.setDefaultNightMode(themeId)
        setTheme(themeId)


        //for the API foreground service
        Intent(applicationContext, APICallService::class.java).also {
            it.action = APICallService.Actions.START.toString()
            startService(it)
        }

        //implement binding
        binding = ActivityFragmentTestingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //nav bar
        drawerLayout = findViewById(R.id.SideNavigation)
        val navView : NavigationView = findViewById(R.id.sideNavigationView)

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        replaceFragment(Sensor_data_fragment(),"Sensor Data")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adminGroup = navView.menu.findItem(R.id.Admin)

        if(!UserID.isNullOrEmpty())
        {

            lifecycleScope.launch {

                var bCheck = FirebaseUtils.isAdminUser(UserID!!)
                  adminGroup.isVisible = bCheck

            }





            navView.setNavigationItemSelectedListener {
                it.isChecked = true
                when(it.itemId){
                    R.id.nav_sensor ->replaceFragment(Sensor_data_fragment(),it.title.toString())
                    R.id.nav_controls ->replaceFragment(Equipment_status_fragment(),it.title.toString())
                    R.id.nav_mystores -> replaceFragment(MyStoreFragment(),it.title.toString())
                    R.id.nav_journal -> replaceFragment(JournalFragment(),it.title.toString())
                    R.id.nav_where_to_shop -> replaceFragment(Permission(),it.title.toString())
                    R.id.nav_feedback -> replaceFragment(feedbackFragment(),it.title.toString())
                    R.id.nav_howToGuides ->replaceFragment(HowToGuidesFragment(),it.title.toString())
                    R.id.nav_settings ->replaceFragment(SettingsFragment(),it.title.toString())
                    R.id.nav_notification_history_recycler ->replaceFragment(NotificationHistory(),it.title.toString())
                    R.id.nav_feedbackReviews -> replaceFragment(AdminFeedbackReview_fragement(),it.title.toString())
                    R.id.nav_FarmerReviews -> replaceFragment(FarmOverViewFragment(),it.title.toString())
                    R.id.nav_logout -> { val intent = Intent(this, GoogleLogin::class.java)


                        SharedPreferencesManager(this).clearUserData()
                        startActivity(intent) }
                }
                true
            }
        }

        else
        {
            adminGroup.isVisible = false
            navView.setNavigationItemSelectedListener {
                it.isChecked = true
                when(it.itemId){
                    R.id.nav_sensor ->replaceFragment(Sensor_data_fragment(),it.title.toString())
                    R.id.nav_controls ->replaceFragment(Equipment_status_fragment(),it.title.toString())
                    R.id.nav_mystores -> Toast.makeText(applicationContext, "Please Sign in to use this feature!", Toast.LENGTH_SHORT).show()
                    R.id.nav_where_to_shop -> Toast.makeText(applicationContext, "Please Sign in to use this feature!", Toast.LENGTH_SHORT).show()
                    R.id.nav_journal -> Toast.makeText(applicationContext, "Please Sign in to use this feature!", Toast.LENGTH_SHORT).show()
                    R.id.nav_feedback -> replaceFragment(feedbackFragment(),it.title.toString())
                    R.id.nav_howToGuides ->replaceFragment(HowToGuidesFragment(),it.title.toString())
                    R.id.nav_settings ->replaceFragment(SettingsFragment(),it.title.toString())
                    R.id.nav_feedbackReviews -> replaceFragment(AdminFeedbackReview_fragement(),it.title.toString())
                    R.id.nav_notification_history_recycler ->replaceFragment(NotificationHistory(),it.title.toString())
                    R.id.nav_logout -> { val intent = Intent(this, GoogleLogin::class.java)


                        SharedPreferencesManager(this).clearUserData()
                        startActivity(intent) }
                }
                true
            }

        }

        fragmentNavigation = this
    }



    override fun onPause() {
        super.onPause()
        SharedPreferencesManager(this).saveUserData(UserName, UserEmail, UserURL, UserID)
        /*val sharedPreferences = getSharedPreferences(GoogleLogin.userLoggedPreference, MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("shUserName_key", UserName)
        editor.putString("shUserEmail_key", UserEmail)
        editor.putString("shUserURL_key", UserURL)
        editor.putString("shUserID_key", UserID)
        editor.apply()*/
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
      override fun replaceFragment(fragment:Fragment,title:String)
     {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.commit()
        drawerLayout.closeDrawers()
        setTitle(title)
    }


}

interface FragmentNavigation {
    fun replaceFragment(fragment: Fragment, title: String)
}



