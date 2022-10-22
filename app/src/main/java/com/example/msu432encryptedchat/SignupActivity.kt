package com.example.msu432encryptedchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.myapplicationchat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity()
{
    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var signup: Button

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_activity)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        name = findViewById(R.id.signup_name)
        email = findViewById(R.id.signup_email)
        password = findViewById(R.id.signup_password)
        signup = findViewById(R.id.signup_register)

        signup.setOnClickListener {
            val name = name.text.toString()
            val email = email.text.toString()
            val password = password.text.toString()


            signUp(name, email, password)
        }
    }

    private fun signUp(name: String, email: String, password: String)
    {
        // Logic
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this)
        { task ->
            if (task.isSuccessful)
            {
                // code for jumping to home
                addUsertoDatabase(name, email, mAuth.currentUser?.uid!!)
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
        mDbRef = FirebaseDatabase.getInstance().getReference()

        mDbRef.child("user").child(uid).setValue(User(name, email, uid))
    }
}