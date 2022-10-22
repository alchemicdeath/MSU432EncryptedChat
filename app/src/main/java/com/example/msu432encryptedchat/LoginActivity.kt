package com.example.msu432encryptedchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import android.widget.Toast
import com.example.myapplicationchat.R


class LoginActivity : AppCompatActivity()
{
    // Text area entries : used to get input information
    lateinit var email: EditText
    lateinit var password: EditText

    // Action buttons to either login or signup
    private lateinit var login: Button
    private lateinit var signup: Button

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
    }

    private fun login(email: String, password: String)
    {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful)
            {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                finish()
                startActivity(intent)
            }
            else
            {
                Toast.makeText(this@LoginActivity, "User does not exist",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
}