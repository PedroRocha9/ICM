package com.example.icmproject1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts

class Lineup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lineup)

        val profile = findViewById<ImageView>(R.id.profile)
        val findBuddy = findViewById<ImageView>(R.id.findBuddy)
        val qrcode = findViewById<ImageView>(R.id.qrcode)
        profile.setOnClickListener {
            goToActivity(Profile::class.java)
        }
        findBuddy.setOnClickListener {
            goToActivity(FindUrBuddies::class.java)
        }
        qrcode.setOnClickListener {
            goToActivity(ScanQRCode::class.java)
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