package com.example.icmproject1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.icmproject1.databinding.ActivityLineupBinding
import com.example.icmproject1.databinding.ActivityMainBinding

class Lineup : AppCompatActivity() {
    private lateinit var binding: ActivityLineupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLineupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(LineupFragment())

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.lineup -> {
                    replaceFragment(LineupFragment())
                    true
                }
                R.id.qrcode -> {
                    replaceFragment(QRCodeFragment())
                    true
                }
                R.id.findBuddy -> {
                    replaceFragment(FindBuddyFragment())
                    true
                }
                else -> false
            }
        }

        binding.userIcon.setOnClickListener {
            goToActivity(ProfileActivity::class.java)
        }
    }

    // replace fragment function
    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
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