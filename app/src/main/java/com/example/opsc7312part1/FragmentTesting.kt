package com.example.opsc7312part1

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.opsc7312part1.databinding.ActivityFragmentTestingBinding
import com.google.android.material.navigation.NavigationView

class FragmentTesting :AppCompatActivity() {

    private lateinit var binding: ActivityFragmentTestingBinding
    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#428948")))

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
            }
            true
        }

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