package com.example.msu432encryptedchat

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity()
{
    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var fireDBRef: DatabaseReference

    private var receiverRoom: String? = null
    private var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_activity)

        // Retrieve extended data from the intent. And sets the value
        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")

        // The entry point of the Firebase Authentication SDK.
        // Returns an instance of this class corresponding to the default FirebaseApp instance.
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid

        // Entry point for firebase database with and instance at the root node
        // Gets the default FirebaseDatabase instance.
        // Gets a DatabaseReference for the database root node.
        fireDBRef = FirebaseDatabase.getInstance().reference

        // Creates the room Id's for sender and receiver
        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        // Set the action bar's title. This will only be displayed if DISPLAY_SHOW_TITLE is set.
        supportActionBar?.title = name

        // Sets the values based on the android id in xml
        chatRecyclerView = findViewById(R.id.charRecylearView)
        messageBox = findViewById(R.id.messageBox)
        sendButton = findViewById(R.id.sendButton)

        // ArrayList of messages
        messageList = ArrayList()

        // RecyclerView.Adapter<ViewHolder>() extension this and arraylist
        messageAdapter = MessageAdapter(this, messageList)

        // Set the RecyclerView.LayoutManager that this RecyclerView will use.
        // Creates a vertical LinearLayoutManager
        chatRecyclerView.layoutManager = LinearLayoutManager(this)

        // Set a new adapter to provide child views on demand.
        // When adapter is changed, all existing views are recycled back to the pool.
        // If the pool has only one adapter, it will be cleared.
        chatRecyclerView.adapter = messageAdapter

        // Get a reference to location relative to this one
        // Add a listener for changes in the data at this location.
        // A DataSnapshot instance contains data from a Firebase Database location.
        //
        fireDBRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object: ValueEventListener{
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {

                    // Removes all of the elements from this list.
                    // The list will be empty after this call returns.
                    messageList.clear()

                    for (postSnapshot in snapshot.children)
                    {
                        // This method is used to marshall the data contained in
                        // this snapshot into a class of your choosing.
                        val message = postSnapshot.getValue(Message::class.java)
                        // Appends the specified element to the end of this list.
                        messageList.add(message!!)
                    }
                    // Notify any registered observers that the data set has changed.
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        // Register a callback to be invoked when this view is clicked.
        sendButton.setOnClickListener {
            // Returns a string representation of the object.
            val message = messageBox.text.toString()
            val messageObject = Message(message, senderUid)

            fireDBRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    fireDBRef.child("chats").child(receiverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            // Sets the text to be displayed.
            messageBox.setText("")
        }
    }
}