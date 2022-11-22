package com.example.msu432encryptedchat

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity()
{
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var fbAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // The entry point of the Firebase Authentication SDK.
        // Returns an instance of this class corresponding to the default FirebaseApp instance.
        fbAuth = FirebaseAuth.getInstance()
        // The entry point for accessing a Firebase Database.
        // Gets the default FirebaseDatabase instance.
        // Gets a DatabaseReference for the database root node.
        mDbRef = FirebaseDatabase.getInstance().reference

        userList = ArrayList()
        // Adapter for Users, extends the RecyclerView ViewHolder
        adapter = UserAdapter(this, userList)

        // Gets the data in the RecyclerView
        userRecyclerView = findViewById(R.id.recyclerView)

        // Set the RecyclerView.LayoutManager that this RecyclerView will use.
        // Creates a vertical LinearLayoutManager
        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter = adapter

        // Get a reference to location relative to this one
        // Add a listener for changes in the data at this location.
        // Each time the data changes, your listener will be called with an immutable snapshot
        // of the data.
        mDbRef.child("user").addValueEventListener(object: ValueEventListener
        {
            // This method will be called with a snapshot of the data at this location.
            // It will also be called each time that data changes.
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot)
            {
                // Removes all of the elements from this list.
                // The list will be empty after this call returns.
                userList.clear()

                // Gives access to all of the immediate children of this snapshot.
                // Can be used in native for loops:
                for(postSnapshot in snapshot.children)
                {
                    // This method is used to marshall the data contained in
                    // this snapshot into a
                    // class of your choosing.
                    val currentUser = postSnapshot.getValue(User::class.java)

                    if (fbAuth.currentUser?.uid != currentUser?.uid)
                    {
                        // Appends the specified element to the end of this list
                        userList.add(currentUser!!)
                    }
                }
                // Notify any registered observers that the data set has changed.
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean
    {
        // Inflate a menu hierarchy from the specified XML resource.
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        if(item.itemId == R.id.logout)
        {
            // Signs out the current user and clears it from the disk cache
            fbAuth.signOut()
            // Intent provides a facility for performing late runtime binding between the code in
            // different applications.
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            return true
        }
        return true
    }
}