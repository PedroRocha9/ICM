package com.example.icmproject1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

class LoginActivity : AppCompatActivity() {
    private var username = findViewById<EditText>(R.id.username)
    private var password = findViewById<EditText>(R.id.password)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val login = findViewById<Button>(R.id.loginButton)
        login.setOnClickListener {
            goToActivity(Lineup::class.java)
        }

        var buttonLogin = findViewById<Button>(R.id.loginButton)
        buttonLogin.setOnClickListener() {
            // Verify is empty
            if (TextUtils.isEmpty(username.text.toString())) {
                Toast.makeText(this, "Please enter username", Toast.LENGTH_SHORT).show()
                username.error = "Please enter username!"
                username.requestFocus()
            }else if(TextUtils.isEmpty(password.text.toString())){
                Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
                password.error = "Please enter password!"
                password.requestFocus()
            } else {

            }
                //goToActivity(Lineup::class.java)
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