package com.example.opsc7312part1

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import java.util.Locale


class AdminFeedbackReview_fragement : Fragment() {

    private lateinit var adminFeedbackReviewAdapter: AdminFeedbackReviewAdapter
    private lateinit var adminFeedbackRecyclerView: RecyclerView
    private lateinit var feedbackList : List<FeedbackData>
    private lateinit var searchView : SearchView
    private lateinit var tvFeedbackErrorMessage : TextView
    private lateinit var myFeedbackLoadingBar : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        feedbackList = emptyList()

        // Inflate the layout for this fragment
        val rootView=  inflater.inflate(R.layout.fragment_admin_feedback_review, container, false)
        (activity as AppCompatActivity).supportActionBar?.apply {
            setHasOptionsMenu(true)
        }
        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.action_refresh_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.RefreshOnly ->{
                FragmentUtils.refreshFragment(requireActivity(), AdminFeedbackReview_fragement(), R.id.frameLayout)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchView = view.findViewById(R.id.searchfeedback)
        adminFeedbackRecyclerView = view.findViewById(R.id.feedbackRecyclerView)
        myFeedbackLoadingBar = view.findViewById(R.id.feedbackLoadingBar)
        tvFeedbackErrorMessage = view.findViewById(R.id.feedbackErrorMessage)

        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })

        feedbackDetailsInitializer()
        val layoutManager = LinearLayoutManager(context)
        adminFeedbackRecyclerView = view.findViewById(R.id.feedbackRecyclerView)
        adminFeedbackRecyclerView.layoutManager = layoutManager
        adminFeedbackRecyclerView.setHasFixedSize(true)
        adminFeedbackReviewAdapter = AdminFeedbackReviewAdapter(feedbackList)
        adminFeedbackRecyclerView.adapter = adminFeedbackReviewAdapter


    }

    private fun feedbackDetailsInitializer() {

            showLoading()
            lifecycleScope.launch {
                feedbackList = FeedbackUtil.getFeedbackForAdmin()
                adminFeedbackReviewAdapter = AdminFeedbackReviewAdapter(feedbackList)
                adminFeedbackRecyclerView.adapter = adminFeedbackReviewAdapter

                if(feedbackList.isEmpty())
                {
                    showError("No Feedback Found...")
                    searchView.visibility = View.GONE
                }
                hideLoading()

        }
    }

    private fun showError(errorMessage: String) {
        tvFeedbackErrorMessage.text = errorMessage
        tvFeedbackErrorMessage.visibility = View.VISIBLE
        adminFeedbackRecyclerView.visibility = View.GONE
    }
    private fun hideError() {
        tvFeedbackErrorMessage.visibility = View.GONE
        adminFeedbackRecyclerView.visibility = View.VISIBLE
    }

    private fun showLoading() {
        myFeedbackLoadingBar.visibility = View.VISIBLE
        hideError()
    }

    private fun hideLoading() {
        myFeedbackLoadingBar.visibility = View.GONE
        adminFeedbackRecyclerView.visibility = View.VISIBLE
    }

    private fun filterList(query: String?) {
        if (query != null){

            var feedback = (mutableListOf <FeedbackData>())
            for(i in feedbackList){
                if(i.title.lowercase(Locale.ROOT).contains(query.lowercase())){
                    feedback.add(i)
                }
            }

            adminFeedbackReviewAdapter.setFilteredist(feedback)
        }
    }


}