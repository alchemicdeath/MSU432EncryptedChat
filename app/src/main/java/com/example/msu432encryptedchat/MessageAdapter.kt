package com.example.msu432encryptedchat


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.auth.FirebaseAuth

// Adapters provide a binding from an app-specific data set to views that are displayed within a RecyclerView.
class MessageAdapter(val context: Context, private val messageList: ArrayList<Message>): RecyclerView.Adapter<ViewHolder>()
{
    private val receive = 1
    private val sent = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        if (viewType == 1)
        {
            // Instantiates a layout XML file into its corresponding View  objects
            // Obtains the LayoutInflater from the given context.
            val view: View = LayoutInflater.from(context)
                .inflate(R.layout.received, parent, false)
            return ReceiveViewHolder(view)
        }
        else
        {
            // Instantiates a layout XML file into its corresponding View  objects
            // Obtains the LayoutInflater from the given context.
            val view: View = LayoutInflater.from(context)
                .inflate(R.layout.sent, parent, false)
            return SentViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        val currentMessage = messageList[position]

        if (holder.javaClass == SentViewHolder::class.java)
        {
            // Send View holder
            holder as SentViewHolder
            holder.sentMessage.text = currentMessage.message
        }
        else
        {
            // Receive View Holder
            holder as ReceiveViewHolder
            holder.receiveMessage.text = currentMessage.message
        }
    }

    override fun getItemViewType(position: Int): Int
    {
        val currentMessage = messageList[position]

        // The entry point of the Firebase Authentication SDK.
        // Returns an instance of this class corresponding to the default FirebaseApp instance.
        // Compares currentUser to senderId
        // Returns true if this string is equal to other, optionally ignoring character case.
        return if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId))
        {
            sent
        }
        else
        {
            receive
        }
    }

    override fun getItemCount(): Int
    {
        // Returns the number of elements in this list.
        return messageList.size
    }

    class SentViewHolder(itemView: View): ViewHolder(itemView)
    {
        // Finds the first descendant view with the given ID, the view itself if the
        // ID matches getId() , or null if the ID is invalid (< 0) or there is no matching view in
        // the hierarchy.
        val sentMessage = itemView.findViewById<TextView>(R.id.txt_sent_message)!!
    }

    class ReceiveViewHolder(itemView: View): ViewHolder(itemView)
    {
        // Finds the first descendant view with the given ID, the view itself if the
        // ID matches getId() , or null if the ID is invalid (< 0) or there is no matching view in
        // the hierarchy.
        val receiveMessage = itemView.findViewById<TextView>(R.id.txt_received_message)!!
    }
}