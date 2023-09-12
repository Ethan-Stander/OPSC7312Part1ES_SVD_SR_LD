package com.example.opsc7312part1

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

//global var to store user details across pages more effective than pulling from database everytime
var UserName: String = ""
var UserEmail: String = ""
var UserURL: String = ""
var UserID: String = ""


class GoogleLogin : AppCompatActivity() {


    //fire base authentication
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var dbref: DatabaseReference

    //progress bar
    private lateinit var progressBar: ProgressBar
    private lateinit var progressBarbackground: ImageView

    //timeout used for loading screen if no response is received
    private val TIMEOUT_DURATION = 2000L // 2 seconds
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_login)

        progressBar = findViewById(R.id.loginProgressBar)
        progressBarbackground = findViewById(R.id.imgloading)


        auth = FirebaseAuth.getInstance()

        //request google sign in
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        findViewById<Button>(R.id.btnGoogleLogin).setOnClickListener {
            signInGoogle()
        }

        findViewById<TextView>(R.id.lblskip).setOnClickListener {
            val intent = Intent(this, FragmentTesting::class.java)
            UserName =""
            startActivity(intent)
        }

    }

    //disables back button on phone default navigation bar
    //don't remove, its works
    override fun onBackPressed() {}

    private fun signInGoogle() {
        showProgressBar()
        googleSignInClient.signOut().addOnCompleteListener {
            // Sign-out successful. Now, proceed with account selection.
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }

        // Set a timeout for the progress bar to be hidden if no response is received
        Handler().postDelayed({
            hideProgressBar()
        }, TIMEOUT_DURATION)
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            try {
                if (result.resultCode == Activity.RESULT_OK) {
                    Log.i("Check result",result.resultCode.toString())
                    val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    handleResults(task)
                }
            } catch (e: Exception) {
                // Handle the exception here
                Log.e("Google login error", "Error occurred: ${e.message}")
            }
        }
    private fun handleResults(task: Task<GoogleSignInAccount>) {
        showProgressBar()
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                updateUI(account)
            } else {
                hideProgressBar()
            }
        } else {
            hideProgressBar()
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun hideProgressBar() {
        progressBarbackground.visibility = View.GONE // Hide the overlay
        progressBar.visibility = View.GONE // Hide the progress bar
    }

    private fun showProgressBar() {
        progressBarbackground.visibility = View.VISIBLE // Hide the overlay
        progressBar.visibility = View.VISIBLE // Hide the progress bar
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.i("Check login succesful", task.isSuccessful.toString())
                dbref = FirebaseDatabase.getInstance().getReference("Users")
                dbref.child(account.id.toString()).get().addOnSuccessListener { dataSnapshot ->
                    if (!dataSnapshot.exists()) {
                        val user = User(
                            UserID = account.id,
                            Username = account.displayName,

                        )

                        val parentJob = Job()
                        val coroutineScope = CoroutineScope(Dispatchers.IO + parentJob)

                        // Perform the database operations within the coroutine
                        coroutineScope.launch {
                            try {
                                dbref = FirebaseDatabase.getInstance().getReference("Users")

                                // Perform the user data insertion
                                dbref.child(UserID).setValue(user).await()

                                val setting = Setting()
                                FirebaseUtils.insertSettingForUser(user, setting)

                                // Display a toast message here or perform other actions as needed
                                Toast.makeText(

                                    this@GoogleLogin,
                                    "Welcome to SmartHydro!",
                                    Toast.LENGTH_LONG
                                ).show()
                            } catch (e: Exception) {
                                // Handle exceptions
                            } finally {
                                parentJob.cancel() // Cancel the parent job when the operations are complete
                            }
                        }
                    }else
                    {
                        Toast.makeText(
                            this@GoogleLogin,
                            "Welcome back to SmartHydro!",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    // Set the necessary data
                    UserName = account.displayName.toString()
                    UserEmail = account.email.toString()
                    UserURL = account.photoUrl.toString()
                    UserID = account.id.toString()

                    // Show the progress bar
                    showProgressBar()

                    // Transition to the next activity after a delay
                    Handler().postDelayed({
                        val intent = Intent(this, FragmentTesting::class.java)
                        startActivity(intent)

                        // Hide the progress bar
                        hideProgressBar()

                    }, 200) // Delay for 2 seconds (adjust as needed)
                }
            } else {
                val exception = task.exception
                if (exception is FirebaseAuthInvalidCredentialsException) {
                    // Invalid credentials (e.g., expired token)
                    // Handle this case
                } else if (exception is FirebaseAuthUserCollisionException) {
                    // User collision (e.g., account already linked)
                    // Handle this case
                } else {
                    // Other exceptions
                    Toast.makeText(
                        this@GoogleLogin,
                        "${exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.e("TAG", "Authentication failed: ${exception?.message}")
                }

                hideProgressBar()
            }
        }
    }


}

