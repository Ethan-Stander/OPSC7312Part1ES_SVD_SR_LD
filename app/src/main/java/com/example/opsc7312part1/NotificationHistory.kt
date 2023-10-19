package com.example.opsc7312part1

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.opsc7312part1.NotificationDataClass.Companion.deleteAllNotificationsForUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class NotificationHistory : Fragment() {

    private lateinit var notification_history_recycler: RecyclerView
    private lateinit var notificationHistoryAdapter: NotificationHistoryAdapter
    private lateinit var btnClearNotifications: ImageButton
    private lateinit var tvSensorErrorMessage : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notification_history, container, false)
        notification_history_recycler = view.findViewById(R.id.notification_history_recycler)
        notification_history_recycler.layoutManager = LinearLayoutManager(activity)
        notificationHistoryAdapter = NotificationHistoryAdapter(emptyList()) // Initially empty
        notification_history_recycler.adapter = notificationHistoryAdapter

        tvSensorErrorMessage = view.findViewById(R.id.tvSensorErrorMessage2)


        // Retrieve notifications and update the adapter when available
        val user = User(
            UserID = UserID,
            Username = UserName
        )
        CoroutineScope(Dispatchers.IO).launch {
            val databaseHandler = context?.let { DatabaseHelper(context = it) }
            val notifications = databaseHandler?.getAllNotifications()
            withContext(Dispatchers.Main) {
                if (notifications != null) {
                    if (!notifications.isEmpty()) {
                        notificationHistoryAdapter = NotificationHistoryAdapter(notifications)
                        notification_history_recycler.adapter = notificationHistoryAdapter
                        hideError()
                    } else {
                        showError("There are no new notifications!")


                    }
                }
                else {
                    showError("Error Loading notifications, please try again!")


                }
            }
        }

        return view
    }

    private fun showError(errorMessage: String) {
        tvSensorErrorMessage.text = errorMessage
        tvSensorErrorMessage.visibility = View.VISIBLE
        notification_history_recycler.visibility = View.GONE // Hide the RecyclerView
    }

    // hide if equipment shows
    private fun hideError() {
        tvSensorErrorMessage.visibility = View.GONE
        notification_history_recycler.visibility = View.VISIBLE
    }

}