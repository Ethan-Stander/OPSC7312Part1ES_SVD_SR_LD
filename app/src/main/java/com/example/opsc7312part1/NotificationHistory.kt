package com.example.opsc7312part1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notification_history, container, false)
        notification_history_recycler = view.findViewById(R.id.notification_history_recycler)
        notification_history_recycler.layoutManager = LinearLayoutManager(activity)
        notificationHistoryAdapter = NotificationHistoryAdapter(emptyList()) // Initially empty
        notification_history_recycler.adapter = notificationHistoryAdapter


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
                    notificationHistoryAdapter = NotificationHistoryAdapter(notifications)
                    notification_history_recycler.adapter = notificationHistoryAdapter
                }
            }
        }

        return view
    }
}