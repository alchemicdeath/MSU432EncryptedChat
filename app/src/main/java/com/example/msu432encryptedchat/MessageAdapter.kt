package com.example.msu432encryptedchat


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.auth.FirebaseAuth

// Adapters provide a binding from an app-specific data set to views that are
// displayed within a RecyclerView.
class MessageAdapter(val context: Context, private val messageList:
                         ArrayList<Message>): RecyclerView.Adapter<ViewHolder>()
{
    private val receive = 1
    private val sent = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder
    {
        // Decides if the message is a sender or receiver message
        return if (viewType == 1) {
            // Obtains the LayoutInflater from the given context.
            val view: View = LayoutInflater.from(context).inflate(
                R.layout.received, parent, false)
            ReceiveViewHolder(view)
        }
        else {
            // Obtains the LayoutInflater from the given context.
            val view: View = LayoutInflater.from(context).inflate(
                R.layout.sent, parent, false)
            SentViewHolder(view)
        }
    }

    // Will Decrypt and Display the message
    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        // Gets the current message
        val currentMessage = messageList[position]

        // Check if the holder is Send or Receiver to display the message
        if (holder.javaClass == SentViewHolder::class.java)
        {
            // Send View holder
            holder as SentViewHolder
            // convert the message to a string
            val msgStr = currentMessage.message.toString()
            //val decrypted = MessageTransform.decryptMessage(msgStr)

            holder.sentMessage.text = MessageTransform.decryptMessage(msgStr)
            // [DEBUGGING] Log.v("MessageAdapter", msgStr)
        }
        else
        {
            // Receive View Holder
            holder as ReceiveViewHolder

            val msgStr = currentMessage.message.toString()
            //val decrypted = MessageTransform.decryptMessage(msgStr)

            // Set the Receiver message to the current message
            holder.receiveMessage.text = MessageTransform.decryptMessage(msgStr)
        }
    }

    // Check to see if the current users ID is the same as the senders ID
    // for the message, then returns if it is.
    override fun getItemViewType(position: Int): Int
    {
        val currentMessage = messageList[position]

        return if (FirebaseAuth.getInstance()
                .currentUser?.uid.equals(currentMessage.senderId)) sent
        else receive
    }

    // Returns the number of elements in this list.
    override fun getItemCount(): Int
    {
        return messageList.size
    }

    class SentViewHolder(itemView: View): ViewHolder(itemView)
    {
        // Returns the sender view holder
        val sentMessage = itemView.findViewById<TextView>(
                                                        R.id.txt_sent_message)!!
    }

    class ReceiveViewHolder(itemView: View): ViewHolder(itemView)
    {
        // Returns the receiver view holder
        val receiveMessage = itemView.findViewById<TextView>(
                                                    R.id.txt_received_message)!!
    }
}