package com.example.opsc7312part1

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*


class NotificationHistory : Fragment() {

    private lateinit var notification_history_recycler: RecyclerView
    private lateinit var notificationHistoryAdapter: NotificationHistoryAdapter
    private lateinit var tvSensorErrorMessage : TextView

    //for datepicker
    private lateinit var btnFilter : Button
    private val calendar = Calendar.getInstance()
    private var filteringDate :String = ""


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

        //for date picker
        btnFilter = view.findViewById(R.id.btnfilter)

        btnFilter.setOnClickListener {

                showDatePicker()

        }

        (activity as AppCompatActivity).supportActionBar?.apply {
            setHasOptionsMenu(true)
        }

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
            val notifications = databaseHandler?.getAllNotifications(filteringDate)
            withContext(Dispatchers.Main) {
                if (notifications != null) {
                    if (!notifications.isEmpty()) {
                        notificationHistoryAdapter = NotificationHistoryAdapter(notifications)
                        notification_history_recycler.adapter = notificationHistoryAdapter
                        hideError()
                    } else {
                        showError("There are no new notifications...")
                    }
                }
                else {
                    showError("Error Loading notifications, please try again...")
                }
            }
        }
        return view
    }

    private fun showDatePicker() {
        val datePicker = DatePickerDialog(
            requireContext(), R.style.CustomDatePickerDialog, {DatePicker, year:Int, monthOfYear: Int, dayOfMonth:Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear,dayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd",Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)
                filteringDate = formattedDate.toString()
                btnFilter.text = filteringDate
                CoroutineScope(Dispatchers.IO).launch {
                    val databaseHandler = context?.let { DatabaseHelper(context = it) }
                    val notifications = databaseHandler?.getAllNotifications(filteringDate)
                    withContext(Dispatchers.Main) {
                        if (notifications != null) {
                            if (!notifications.isEmpty()) {
                                notificationHistoryAdapter =
                                    NotificationHistoryAdapter(notifications)
                                notification_history_recycler.adapter = notificationHistoryAdapter
                                hideError()
                            } else {
                                showError("There are no new notifications...")
                            }
                        } else {
                            showError("Error Loading notifications, please try again...")
                        }
                    }
                }
            }, calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.action_refresh_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.RefreshOnly ->{
                FragmentUtils.refreshFragment(requireActivity(), NotificationHistory(), R.id.frameLayout)
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
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