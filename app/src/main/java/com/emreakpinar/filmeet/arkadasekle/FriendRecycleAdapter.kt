package com.emreakpinar.filmeet.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.emreakpinar.filmeet.arkadasekle.User
import com.emreakpinar.filmeet.databinding.ItemFriendBinding
class FriendRecyclerAdapter(
    private var friends: List<User>,
    private val onDeleteClick: (User) -> Unit,
    private val onChatClick: (User) -> Unit
) : RecyclerView.Adapter<FriendRecyclerAdapter.FriendViewHolder>() {
    class FriendViewHolder(val binding: ItemFriendBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val binding = ItemFriendBinding
            .inflate(LayoutInflater.from(parent.context)
                , parent, false)
        return FriendViewHolder(binding)
    }
    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val friend = friends[position]
        holder.binding.friendTextView.text = friend.username
        holder.binding.deleteButton.setOnClickListener {
            onDeleteClick(friend)
        }
        holder.binding.chatButton.setOnClickListener {
            onChatClick(friend)
        }
    }
    override fun getItemCount(): Int {
        return friends.size
    }
    fun updateFriends(newFriends: List<User>) {
        friends = newFriends
        notifyDataSetChanged()
    }
}
