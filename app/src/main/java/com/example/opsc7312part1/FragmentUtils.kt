package com.example.opsc7312part1

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class FragmentUtils {

    fun refreshFragment(context:Context?, currentFragment: Fragment){
        context?.let {
            val fragmentManager = (context as? AppCompatActivity)?.supportFragmentManager
            fragmentManager?.let {
                    val fragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.detach(currentFragment)
                    fragmentTransaction.attach(currentFragment)
                    fragmentTransaction.commit()
            }
        }
    }
}