package com.example.icmproject1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts

class NaughtyPermission : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_naughty_permission)

        val profile = findViewById<ImageView>(R.id.profile)
        val qrcode = findViewById<ImageView>(R.id.qrcode)
        val lineup = findViewById<ImageView>(R.id.lineup)
        val findBuddy = findViewById<ImageView>(R.id.findBuddy)
        profile.setOnClickListener {
            goToActivity(Profile::class.java)
        }
        lineup.setOnClickListener {
            goToActivity(Lineup::class.java)
        }
        qrcode.setOnClickListener {
            goToActivity(QRCodePrompt::class.java)
        }
        findBuddy.setOnClickListener {
            goToActivity(FindUrBuddies::class.java)
        }

        val participate = findViewById<TextView>(R.id.participate)
        val notParticipate = findViewById<TextView>(R.id.notParticipate)
        participate.setOnClickListener {
            goToActivity(TakingPicNaughty::class.java)
        }
        notParticipate.setOnClickListener {
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