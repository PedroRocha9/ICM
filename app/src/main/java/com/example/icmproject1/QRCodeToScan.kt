package com.example.icmproject1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts

class QRCodeToScan : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode_to_scan)

        val profile = findViewById<ImageView>(R.id.profile)
        val findBuddy = findViewById<ImageView>(R.id.findBuddy)
        val qrcode = findViewById<ImageView>(R.id.qrcode)
        val lineup = findViewById<ImageView>(R.id.lineup)
        profile.setOnClickListener {
            goToActivity(Profile::class.java)
        }
        lineup.setOnClickListener {
            goToActivity(Lineup::class.java)
        }
        findBuddy.setOnClickListener {
            goToActivity(FindUrBuddies::class.java)
        }
        qrcode.setOnClickListener {
            goToActivity(QRCodePrompt::class.java)
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