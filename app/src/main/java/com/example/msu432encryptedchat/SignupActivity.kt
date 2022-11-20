package com.example.msu432encryptedchat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


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

    // Add the user to the Firebase Database
    private fun addUsertoDatabase(name: String, email: String, uid: String)
    {
        mDbRef = FirebaseDatabase.getInstance().getReference()

        // Get the creation of the users public and private keys
        val keys = GenerateUserKeys.generateKeys()
        val e = keys[0]
        val n = keys[1]
        var d = keys[2]
        val pub = "$e-$n"
        Log.e("$e-$n","-$d")

        // Set the files name
        val fileName = "$uid.txt"
        // val of the decryption key
        val string1 = keys[2].toString() + "\n" + keys[1].toString()

        var outFile : FileOutputStream? = null
        // writes the data to the files
        try
        {
            outFile = openFileOutput(fileName, MODE_PRIVATE)
            outFile.write(string1.toByteArray())

            Toast.makeText(this,"Saved File to $filesDir/$fileName",
                Toast.LENGTH_LONG).show()

        }
        catch (e: FileNotFoundException)
        {
            e.printStackTrace()
        }
        catch (e: Error)
        {
            e.printStackTrace()
        }
        finally {
            if(outFile != null)
            {
                try {
                    outFile.close()
                }
                catch (e : IOException)
                {
                    e.printStackTrace()
                }
            }
        }

        // Add the users data to the database
        mDbRef.child("user").child(uid)
                                    .setValue(User(name, email, uid, pub))
    }
}