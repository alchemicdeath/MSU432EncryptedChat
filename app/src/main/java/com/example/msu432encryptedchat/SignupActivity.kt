package com.example.msu432encryptedchat

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.security.PublicKey


class SignupActivity : AppCompatActivity()
{
    // Text input variables
    private lateinit var nameText: EditText
    private lateinit var emailText: EditText
    private lateinit var passwordText: EditText

    // Sign up button
    private lateinit var signup: Button

    // Firebase connections
    private lateinit var auth : FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_layout)

        // Hide action bar
        supportActionBar?.hide()

        // Instance for Firebase Authentication SDK
        auth = FirebaseAuth.getInstance()

        // Get the text information and use it for sign up
        nameText = findViewById(R.id.signup_name)
        emailText = findViewById(R.id.signup_email)
        passwordText = findViewById(R.id.signup_password)

        // Button for SignUp
        signup = findViewById(R.id.signup_register)

        // SignUp on Click
        signup.setOnClickListener {
            val name = nameText.text.toString()
            val email = emailText.text.toString()
            val password = passwordText.text.toString()

            // Call signup function
            signUp(name, email, password)
        }
    }

    // SignUp Function
    private fun signUp(name: String, email: String, password: String)
    {
        // Logic
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this)
        { task ->
            if (task.isSuccessful)
            {
                val publicKey = CryptoMethod().genKeys()
                // code for jumping to home
                addUsertoDatabase(name, email, auth.currentUser?.uid!!, publicKey.toString())
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

    // Add the user to the Firebase Database
    private fun addUsertoDatabase(name: String, email: String, uid: String, public : String)
    {
        mDbRef = FirebaseDatabase.getInstance().getReference()

        mDbRef.child("user").child(uid).setValue(User(name, email, uid, public))
    }
}