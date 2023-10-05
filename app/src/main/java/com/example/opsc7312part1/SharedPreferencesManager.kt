package com.example.opsc7312part1

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(context: Context) {

    private val userLoginSharedPreferences: SharedPreferences = context.getSharedPreferences("UserLoginPreferences", Context.MODE_PRIVATE)


    fun clearUserData() {
        val editor = userLoginSharedPreferences.edit()
        editor.clear()
        editor.apply()
        UserName = null
        UserEmail = null
        UserID = null
        UserURL = null
    }

    fun saveUserData(userName: String?, userEmail: String?, userURL: String?, userID: String?) {
        val editor = userLoginSharedPreferences.edit()
        editor.putString("shUserName_key", userName)
        editor.putString("shUserEmail_key", userEmail)
        editor.putString("shUserURL_key", userURL)
        editor.putString("shUserID_key", userID)
        editor.apply()
    }

    fun getUserData(): List<String?> {
        val userName = userLoginSharedPreferences.getString("shUserName_key", "")
        val userEmail = userLoginSharedPreferences.getString("shUserEmail_key", "")
        val userURL = userLoginSharedPreferences.getString("shUserURL_key", "")
        val userID = userLoginSharedPreferences.getString("shUserID_key", "")

        return listOf(userName, userEmail, userURL, userID)
    }
}