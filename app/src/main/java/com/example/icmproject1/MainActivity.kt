package com.example.icmproject1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.ktx.firestore

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            goToActivity(Lineup::class.java)
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Check if user is signed in (non-null) and update UI accordingly.
        auth = Firebase.auth

        // To register
        val register = findViewById<Button>(R.id.registerButtonMain)
        register.setOnClickListener {
            goToActivity(RegisterActivity::class.java)
        }

        // To login
        val login = findViewById<Button>(R.id.loginButton)
        login.setOnClickListener {
            goToActivity(LoginActivity::class.java)
        }

    }

    private val activityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // if returning to this activity, send to lineUp
    }

    private fun goToActivity(activity: Class<*>, fragmentName: String = "lineup") {
        val intent = Intent(this, activity)
        intent.putExtra("fragment", fragmentName)
        activityLauncher.launch(intent)
    }
}