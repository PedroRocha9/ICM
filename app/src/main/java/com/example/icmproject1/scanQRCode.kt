package com.example.icmproject1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts

class ScanQRCode : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_qrcode)

        val profile = findViewById<ImageView>(R.id.profile)
        val qrcode = findViewById<ImageView>(R.id.qrcode)
        val lineup = findViewById<ImageView>(R.id.lineup)
        val findPartner = findViewById<ImageView>(R.id.findPartner)
        val findBuddy = findViewById<ImageView>(R.id.findBuddy)
        profile.setOnClickListener {
            goToActivity(Profile::class.java)
        }
        lineup.setOnClickListener {
            goToActivity(Lineup::class.java)
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