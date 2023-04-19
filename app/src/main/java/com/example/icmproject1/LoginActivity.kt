package com.example.icmproject1

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var email = findViewById<EditText>(R.id.email)
        var password = findViewById<EditText>(R.id.password)
        var auth = FirebaseAuth.getInstance()

        // Doesn't have account yet
        var noAccountYet = findViewById<TextView>(R.id.dontHaveAccount)
        noAccountYet.setOnClickListener {
            goToActivity(RegisterActivity::class.java)
            finish()
        }

        var login = findViewById<Button>(R.id.loginButton)
        login.setOnClickListener {
            if (TextUtils.isEmpty(email.text.toString())) {
                Toast.makeText(this, "Please enter your username!", Toast.LENGTH_SHORT).show()
                email.error = "Please enter username!"
                email.requestFocus()
            }else if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
                Toast.makeText(this, "Please enter a valid e-mail", Toast.LENGTH_SHORT).show()
                email.error = "Please enter a valid e-mail!"
                email.requestFocus()
            }
            if (TextUtils.isEmpty(password.text.toString())) {
                Toast.makeText(this, "Please enter your password!", Toast.LENGTH_SHORT).show()
                password.error = "Please enter password!"
                password.requestFocus()
            }

            auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(baseContext, "Login was a success.", Toast.LENGTH_SHORT).show()
                        val user = auth.currentUser
                        goToActivity(Lineup::class.java)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(baseContext, "Login failed.", Toast.LENGTH_SHORT).show()
                    }
                }
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