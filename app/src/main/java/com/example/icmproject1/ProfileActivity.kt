package com.example.icmproject1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        var auth = FirebaseAuth.getInstance()
        var user = auth.currentUser

        // log its email
        Log.d("ProfileActivity", "User email is ${user?.email}")
        var logOut = findViewById<Button>(R.id.logOut)
        logOut.setOnClickListener {
            // check if user is null before signing out
            if (user != null) {
                auth.signOut()
                goToActivity(MainActivity::class.java)
                finish()
            }else{                  // defensive code
                goToActivity(MainActivity::class.java)
                finish()
            }
        }
    }

    private val activityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // do something if returning to this activity
    }

    private fun goToActivity(activity: Class<*>) {
        val intent = Intent(this, activity)
        activityLauncher.launch(intent)
    }
}