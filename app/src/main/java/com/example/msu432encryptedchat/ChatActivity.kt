package com.example.msu432encryptedchat

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.io.File

class ChatActivity : AppCompatActivity()
{
    // Vars to hold data from the screen:
    /**
        * Each item is to correspond to the appropriate item in the
        * chat_activity.xml
    */
    private lateinit var recyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>

    /******************* The user on the phone is the Sender ****************/
    // Variables for each public/private n,e,d
    var sendD = 0
    var sendN = 0
    var sendE = 0
    var recE = 0
    var recN = 0

    // Firebase db reference
    private lateinit var fireDBRef: DatabaseReference

    private var receiverRoom: String? = null
    private var senderRoom: String? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_activity)
        // Sets the recipient name and their ID
        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")

        // Sets the senderID to the current user ID
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid

        // Gets a DatabaseReference for the database root node.
        fireDBRef = FirebaseDatabase.getInstance().reference

        // Retrieve the sender and receivers keys(n,e,d)
        getPrivateKey(senderUid!!)
        getPublicKey(receiverUid!!)
        getPublicE(senderUid)

        // Creates the room Id's for sender and receiver
        senderRoom = receiverUid + senderUid
        receiverRoom = senderUid + receiverUid

        // Set the action bar's title.
        supportActionBar?.title = name

        // Sets the values based on the android id in xml
        recyclerView = findViewById(R.id.chatRecyclerView)
        messageBox = findViewById(R.id.chatMessageBox)
        sendButton = findViewById(R.id.chatSendButton)

        // ArrayList of messages
        messageList = ArrayList()

        // Gets an instance of a MessageAdapter with the messageList
        messageAdapter = MessageAdapter(this, messageList)

        // Set layout to vertical
        recyclerView.layoutManager = LinearLayoutManager(this)

        // sets the adapter to the messageAdapter
        recyclerView.adapter = messageAdapter

        // Used to shorten repetitive code
        val senderChat = fireDBRef.child("chats").child(senderRoom!!)
            .child("messages")
        val receiverChat = fireDBRef.child("chats")
            .child(receiverRoom!!).child("messages")

        // Gets the messages from firebase to display
        senderChat.addValueEventListener(object:ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot)
            {
                MessageTransform.setValues(sendD.toDouble(), sendN.toDouble())
                // Removes all of the elements from this list.
                messageList.clear()

                for (postSnapshot in snapshot.children)
                {
                    // Gathers the data contained in this snapshot into the
                    // Message class
                    val message = postSnapshot.getValue(Message::class.java)

                    // Appends the message to the end of the messageList.
                     messageList.add(message!!)
                }
                // Notify observers the data has changed
                messageAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {}
        })

       senderChat.addValueEventListener(object: ValueEventListener{
           @SuppressLint("NotifyDataSetChanged")
           override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()

                for (postSnapshot in snapshot.children)
                {
                    val message = postSnapshot.getValue(Message::class.java)
                    Log.v("LOADING*******#####", message.toString())
                    messageList.add(message!!)
                }
                messageAdapter.notifyDataSetChanged()
           }
           override fun onCancelled(error: DatabaseError){}
       })
        // Register a callback to be invoked when this view is clicked.
        sendButton.setOnClickListener{
            // Returns a string representation of the object.
            val message = messageBox.text.toString()
            // Encrypt the message
            val msgReceiver = MessageTransform.encryptMessage(recE,recN,message)
            val msgSender = MessageTransform.encryptMessage(sendE,sendN,message)
            Log.v("Sender $msgSender", "Receiver $msgReceiver")

            // Set message to an object
            /* ### [Debugging]  val messageObject = Message(message,
            senderUid) */
            val senderMessageObject = Message(msgSender, senderUid)
            val receiverMessageObject = Message(msgReceiver, senderUid)

            /* ###[DEBUGGING Log.v("Public M: $pubE:$pubN",
            "Private M:$prvD:$prvN")*/

            // Add message to database, first to the senders room
            senderChat.push().setValue(senderMessageObject)
                .addOnSuccessListener{
                // The second sends to the receivers room, encrypted with
                // their public key

                receiverChat.push().setValue(receiverMessageObject)
            }
            // "Clears the text box"
            messageBox.setText("")
        }
    }

    fun setD(): Double
    {
        return sendD.toDouble()
    }
    fun setN(): Double
    {
        return sendN.toDouble()
    }

    private fun getPrivateKey(uidSender: String)
    {
        Log.v("Sender ID", uidSender)
        // Get the file to load and set the values of the senders d and n keys
        val inStream = File("$filesDir/$uidSender.txt").inputStream()
        val list = mutableListOf<String>()            // list to hold the values
        // Reads the lines to add the values to the list
        inStream.bufferedReader().forEachLine {
            list.add(it)
        }
        // Set the values of the Senders keys (d, n)
        sendD = list[0].toInt()
        sendN = list[1].toInt()
        /* ### {DEBUGGING] Log.v("$prvD", "$prvN") */
    }
    private fun getPublicKey(uidReceiver: String)
    {
        fireDBRef.child("user").child(uidReceiver)
            .child("public")
            .addValueEventListener(object:ValueEventListener
        {
            // Strings to be removed from the child node
            val preFix = "DataSnapshot { key = public, value = "
            val suffix = " }"
            @Override
            override fun onDataChange(snapshot: DataSnapshot) {
                var text : String = snapshot.toString()
                // Removes the prefix and suffix (listed above) from the
                // string of the snapshot
                text = text.replace(preFix, "")
                text = text.replace(suffix, "")
                // Splits the string into an array to get the integers
                val stringArray = text.split("-").toTypedArray()
                // Sets the receivers e and n val
                recE = stringArray[0].toInt()
                recN = stringArray[1].toInt()
                /* ### [DEBUGGING Log.v("Public", "$pubE,$pubN") */
            }
            override fun onCancelled(error: DatabaseError){}
        })
    }
    private fun getPublicE(uidSender: String)
    {
        fireDBRef.child("user").child(uidSender).child(
            "public").addValueEventListener(object:ValueEventListener {
            // Strings to be removed from the child node
            val preFix = "DataSnapshot { key = public, value = "
            val suffix = " }"

            @Override
            override fun onDataChange(snapshot: DataSnapshot) {
                var text : String = snapshot.toString()
                // Removes the prefix and suffix (listed above) from the
                // string of the snapshot
                text = text.replace(preFix, "")
                text = text.replace(suffix, "")
                // Splits the string into an array to get the integers
                val stringArray = text.split("-").toTypedArray()
                // Sets the senders e val
                sendE = stringArray[0].toInt()
                /* ### [DEBUGGING Log.v("Private ", "$prvE") */
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}