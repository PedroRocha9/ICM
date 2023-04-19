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
import com.example.icmproject1.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.w3c.dom.Text

class RegisterActivity : AppCompatActivity() {
        private var auth = FirebaseAuth.getInstance();
        private var dbRef = FirebaseDatabase.getInstance().getReference("Users")
        private lateinit var username : EditText
        private lateinit var email : EditText
        private lateinit var password : EditText
        private lateinit var confirmPassword : EditText
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        // Already Has Account
        val alreadyHasAccount = findViewById<TextView>(R.id.alreadyHaveAccount)
        alreadyHasAccount.setOnClickListener {
            goToActivity(LoginActivity::class.java)
            finish()
        }

        username = findViewById<EditText>(R.id.username)
        email = findViewById<EditText>(R.id.email)
        password = findViewById<EditText>(R.id.password)
        confirmPassword = findViewById<EditText>(R.id.confirmPassword)

        val register = findViewById<Button>(R.id.registerButton)
        register.setOnClickListener {
            if (TextUtils.isEmpty(username.text.toString())) {
                Toast.makeText(this, "Please enter your username!", Toast.LENGTH_SHORT).show()
                username.error = "Please enter username!"
                username.requestFocus()
            }else if (TextUtils.isEmpty(email.text.toString())) {
                Toast.makeText(this, "Please enter your e-mail", Toast.LENGTH_SHORT).show()
                email.error = "Please enter e-mail!"
                email.requestFocus()
            }else if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
                Toast.makeText(this, "Please enter a valid e-mail", Toast.LENGTH_SHORT).show()
                email.error = "Please enter a valid e-mail!"
                email.requestFocus()
            }else if (TextUtils.isEmpty(password.text.toString())){
                Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show()
                password.error = "Please enter password!"
                password.requestFocus()
            }else if (password.length() < 6){
                Toast.makeText(this, "Password too weak", Toast.LENGTH_SHORT).show()
                password.error = "Password too weak!"
                password.requestFocus()
            }else if (TextUtils.isEmpty(confirmPassword.text.toString())) {
                Toast.makeText(this, "Please confirm your password", Toast.LENGTH_SHORT).show()
                confirmPassword.error = "Please confirm your password!"
                confirmPassword.requestFocus()
            }else if ((password.text.toString()) != confirmPassword.text.toString()){
                // print to the terminal the values of password and confirmPassword and the result of the expression
                password.clearComposingText()
                confirmPassword.clearComposingText()
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                confirmPassword.error = "Passwords do not match!"
                confirmPassword.requestFocus()
            }else{
                auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(this, "Registration successful.", Toast.LENGTH_SHORT).show()
                            // print to the terminal the values of email and username that just registered
                            Log.d(TAG, "createUserWithEmail:success")
                            val user = auth.currentUser
                            Log.d(TAG, "User: $user")
                            Log.d(TAG, "Email: ${email.text.toString()}")
                            Log.d(TAG, "Username: ${username.text.toString()}")

                            saveUserToFirebaseDatabase()

                            // go to add festivals activity
                            goToActivity(AddFestivals::class.java)
                            finish()
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                            // go to register again
                            goToActivity(RegisterActivity::class.java)
                        }
                }
            }
        }
    }

    private fun saveUserToFirebaseDatabase() {
        val userId = dbRef.push().key!!
        val newUser = UserModel(userId, username.text.toString(), email.text.toString(), password.text.toString(), null, null, null, null, null)

        dbRef.child(userId).setValue(newUser).addOnCompleteListener {
            Log.e(TAG,"Ques qui se passe? Devia ter seguido")
            Toast.makeText(this, "User saved successfully", Toast.LENGTH_SHORT).show()
            Log.e(TAG, "User saved successfully")
        }.addOnFailureListener{err ->
            Toast.makeText(this, "Error ${err.message} while saving user", Toast.LENGTH_SHORT).show()
            Log.e(TAG, "Error saving user")
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
