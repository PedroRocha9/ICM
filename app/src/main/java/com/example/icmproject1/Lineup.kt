package com.example.icmproject1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
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
                R.id.findPartner -> {
                    replaceFragment(FindPartnerFragment())
                    true
                }
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
    }

    // replace fragment function
    fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }
}