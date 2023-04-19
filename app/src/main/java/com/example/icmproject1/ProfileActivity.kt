package com.example.icmproject1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.icmproject1.databinding.ActivityLineupBinding
import com.example.icmproject1.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(ProfileFragment())

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            val fragmentName = when (item.itemId) {
                R.id.lineup -> "lineup"
                R.id.qrcode -> "qrcode"
                R.id.findBuddy -> "findBuddy"
                else -> "lineup"
            }
            Log.d("ProfileActivity", "Fragment name is $fragmentName")
            goToActivity(Lineup::class.java, fragmentName)

            return@setOnItemSelectedListener true
        }

        var auth = FirebaseAuth.getInstance()
        var user = auth.currentUser
        // log its email
        Log.d("ProfileActivity", "User email is ${user?.email}")
        var logOut = binding.logOut
        logOut.setOnClickListener {
            // check if user is null before signing out
            if (user != null) {
                auth.signOut()
                goToActivity(MainActivity::class.java)
                finish()
            }else{                  // defensive code
                goToActivity(MainActivity::class.java)
                finish()
            }
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
    private fun goToActivity(activity: Class<*>, fragmentName: String = "lineup") {
        val intent = Intent(this, activity)
        intent.putExtra("fragment", fragmentName)
        activityLauncher.launch(intent)
    }

}