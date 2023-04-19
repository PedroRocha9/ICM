package com.example.icmproject1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.icmproject1.databinding.ActivityLineupBinding

class Lineup : AppCompatActivity() {
    private lateinit var binding: ActivityLineupBinding
    private var frag : Fragment = LineupFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLineupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        frag = when (intent.getStringExtra("fragment") ?: "lineup") {
            "lineup" -> LineupFragment()
            "qrcode" -> QRCodeFragment()
            "findBuddy" -> FindBuddyFragment()
            else -> LineupFragment()
        }
        replaceFragment(frag)

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.lineup -> {
                    frag = LineupFragment()
                    true
                }
                R.id.qrcode -> {
                    frag = QRCodeFragment()
                    true
                }
                R.id.findBuddy -> {
                    frag = FindBuddyFragment()
                    true
                }
                else -> false
            }
            replaceFragment(frag)

            return@setOnItemSelectedListener true
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

        // update top section title
        val title = when (fragment) {
            is LineupFragment -> "Lineup"
            is QRCodeFragment -> "QR Code"
            is FindBuddyFragment -> "Find Buddy"
            else -> "Lineup"
        }
        binding.toolbarTitle.text = title
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