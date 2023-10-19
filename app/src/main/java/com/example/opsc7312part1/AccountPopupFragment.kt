package com.example.opsc7312part1

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.opsc7312part1.FirebaseUtils.Companion.deleteUser
import com.example.opsc7312part1.databinding.FragmentAccountPopupBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AccountPopupFragment : DialogFragment() {

    private var _binding: FragmentAccountPopupBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAccountPopupBinding.inflate(inflater, container, false)

        if(UserID == "")
        {
            binding.txtAccountPopUpError.visibility = View.VISIBLE
            binding.accountPopUpImage.visibility = View.GONE
            binding.lvUsernameInformation.visibility = View.GONE
            binding.lvGmailInformation.visibility = View.GONE
            binding.btnDeleteAccount.visibility = View.GONE

        }else
        // User object
        {
            val user = User(
                UserID = UserID,
                Username = UserName
            )
            binding.btnDeleteAccount.setOnClickListener {

                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Delete Account")
                builder.setMessage("Are you sure you want to delete your account?")

                builder.setPositiveButton("Yes") { dialog, _ ->
                    lifecycleScope.launch {

                        val isDeleted = deleteUser(user)

                        if (isDeleted) {
                            SharedPreferencesManager(requireActivity()).clearUserData()
                            val googleSignInIntent = Intent(requireContext(), GoogleLogin::class.java)
                            startActivity(googleSignInIntent)
                        } else {
                            Toast.makeText(requireActivity(), "Failed to delete user", Toast.LENGTH_SHORT).show()
                        }
                    }

                    dialog.dismiss()
                }

                builder.setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }

                val dialog = builder.create()
                dialog.show()
            }





            var userPFP = binding.accountPopUpImage

            Picasso.get()
                .load(UserURL)
                .into(userPFP)

            getUserAccountInfo(user)
        }


        //close popup
        binding.btnAccountClose.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        }

        return binding.root
    }

    fun getUserAccountInfo(user: User?) {
        user?.let {
            lifecycleScope.launch {
                val setting = FirebaseUtils.Get(user)
                setting?.let {
                    // Update the TextView elements with the retrieved information
                    binding.tvUsername.text = UserName
                    binding.tvGmail.text = UserEmail
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AccountPopupFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AccountPopupFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}