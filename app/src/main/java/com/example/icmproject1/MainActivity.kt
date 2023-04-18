package com.example.icmproject1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // To register
        val register = findViewById<Button>(R.id.registerButton)
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
        goToActivity(Lineup::class.java)
    }

    private fun goToActivity(activity: Class<*>) {
        val intent = Intent(this, activity)
        activityLauncher.launch(intent)
    }
}