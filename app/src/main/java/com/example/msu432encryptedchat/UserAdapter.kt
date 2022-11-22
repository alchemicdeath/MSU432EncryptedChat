package com.example.msu432encryptedchat


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
    * Adapter for Users, extends the RecyclerView ViewHolder
    * A subclass of RecyclerView.Adapter responsible for providing views that
    * represent items in a data set.
*/
class UserAdapter(val context: Context, private val userList: ArrayList<User>):
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            UserViewHolder
    {
        // Instantiates a layout XML file into its corresponding View  objects.
        // It is never used directly.
        // Obtains the LayoutInflater from the given context.
        val view: View = LayoutInflater.from(context).inflate(
            R.layout.user_layout, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int)
    {
        // currentUser is set to the userlists current position
        val currentUser = userList[position]

        // Sets the user name to the current users name which you can do with
        // text strings in XML resource files currentUser is set to the
        // userlists current name
        holder.textName.text = currentUser.name

        // Onclick for entering chat with selected user
        holder.itemView.setOnClickListener {

            val intent = Intent(context, ChatActivity::class.java)

            intent.putExtra("name", currentUser.name)
            intent.putExtra("uid", currentUser.uid)

            context.startActivity(intent)
        }
    }

    // Returns the number of elements in this list.
    override fun getItemCount(): Int {
        return userList.size
    }

    // A ViewHolder describes an item view and metadata about its place within
    // the RecyclerView.
    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        // Finds the first descendant view with the given ID, the view itself
        // if the ID matches getId() , or null if the ID is invalid (< 0) or
        // there is no matching view in the hierarchy.
        // Sets the value to the id's value
        val textName: TextView = itemView.findViewById(R.id.txt_name)
    }
}