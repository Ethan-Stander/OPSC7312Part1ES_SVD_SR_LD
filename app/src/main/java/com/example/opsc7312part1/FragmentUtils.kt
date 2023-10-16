package com.example.opsc7312part1

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class FragmentUtils {
    companion object {
        fun refreshFragment(activity: FragmentActivity, fragment: Fragment, containerId: Int) {
            val fragmentManager: FragmentManager = activity.supportFragmentManager
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()

            // Create a new instance of the fragment
            val newFragment = fragment.javaClass.newInstance()

            // Replace the existing fragment with the new one
            transaction.replace(containerId, newFragment)

            // Commit the transaction
            transaction.commit()
        }
    }
}