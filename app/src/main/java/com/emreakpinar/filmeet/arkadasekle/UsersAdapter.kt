package com.emreakpinar.filmeet.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.emreakpinar.filmeet.R
import com.emreakpinar.filmeet.arkadasekle.User

class UsersAdapter(private val users: List<User>,
                   private val onAddFriendClick: (User) -> Unit) :
    RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {
    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameTextView: TextView = itemView.findViewById(R.id.usernameText)
        val addFriendButton: TextView = itemView.findViewById(R.id.addFriendButton)
        fun bind(user: User) {
            usernameTextView.text = user.username
            addFriendButton.setOnClickListener { onAddFriendClick(user) }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_row, parent, false)
        return UserViewHolder(view)
    }
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }
    override fun getItemCount(): Int {
        return users.size
    }
}
