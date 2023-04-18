package com.example.icmproject1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // To choose festival
        val register = findViewById<Button>(R.id.registerButton)
        register.setOnClickListener {
            goToActivity(AddFestivals::class.java)
        }

        //!Patterns.EMAIL_ADDRESS.matcher(email).matches()

        // password.length() < 6 --> error (too weak)

        // password != confirmPassword --> !password.equals(confirmPassword)
        // clear the entered password and confirm password
        // password.clearComposingText();
        // confirmPassword.clearComposingText();

        // if valid
        //registerUser(username, email, password)
    }

    private fun registerUser(username: Any, email: Any, password: Any) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithUsernameAndPassword(username, email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

    private val activityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // if returning to this activity,
    }

    private fun goToActivity(activity: Class<*>) {
        val intent = Intent(this, activity)
        activityLauncher.launch(intent)
    }
}
