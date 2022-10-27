package com.example.msu432encryptedchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity()
{
    // Text input variables
    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText

    // Sign up button
    private lateinit var signup: Button

    // Firebase connections
    private lateinit var auth : FirebaseAuth
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_layout)

        // Hide action bar
        supportActionBar?.hide()

        // Get the text information and use it for sign up
        auth = FirebaseAuth.getInstance()

        name = findViewById(R.id.signup_name)
        email = findViewById(R.id.signup_email)
        password = findViewById(R.id.signup_password)
        signup = findViewById(R.id.signup_register)

        // sign up action
        signup.setOnClickListener {
            val name = name.text.toString()
            val email = email.text.toString()
            val password = password.text.toString()

            // Call signup function
            signUp(name, email, password)
        }
    }

    private fun signUp(name: String, email: String, password: String)
    {
        // Logic
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this)
        { task ->
            if (task.isSuccessful)
            {
                // code for jumping to home
                addUsertoDatabase(name, email, auth.currentUser?.uid!!)
                val intent = Intent(this@SignupActivity, MainActivity::class.java)
                finish()
                startActivity(intent)
            }
            else
            {
                Toast.makeText(this@SignupActivity, "Some Error occurred",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addUsertoDatabase(name: String, email: String, uid: String)
    {
        database = FirebaseDatabase.getInstance().getReference()

        database.child("user").child(uid).setValue(User(name, email, uid))
    }
}