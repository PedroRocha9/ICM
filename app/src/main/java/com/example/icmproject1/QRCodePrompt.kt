package com.example.icmproject1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts

class QRCodePrompt : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode_prompt)

        val showQRCode = findViewById<Button>(R.id.showQRCode)
        val scanQRCode = findViewById<Button>(R.id.scanQRCode)
        showQRCode.setOnClickListener {
            goToActivity(QRCodeToScan::class.java)
        }
        scanQRCode.setOnClickListener {
            goToActivity(ScanQRCode::class.java)
        }

        val profile = findViewById<ImageView>(R.id.profile)
        val lineup = findViewById<ImageView>(R.id.lineup)
        val findPartner = findViewById<ImageView>(R.id.findPartner)
        val findBuddy = findViewById<ImageView>(R.id.findBuddy)
        profile.setOnClickListener {
            goToActivity(Profile::class.java)
        }
        lineup.setOnClickListener {
            goToActivity(Lineup::class.java)
        }
        findPartner.setOnClickListener {
            goToActivity(NaughtyPermission::class.java)
        }
        findBuddy.setOnClickListener {
            goToActivity(FindUrBuddies::class.java)
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