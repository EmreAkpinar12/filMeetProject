package com.emreakpinar.filmeet.messages

import com.emreakpinar.filmeet.adapter.Chat
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.emreakpinar.filmeet.R
import com.google.firebase.auth.FirebaseAuth

class ChatRecyclerAdapter : RecyclerView.Adapter<ChatRecyclerAdapter.ChatHolder>() {

    private val VIEW_TYPE_MESSAGE_SENT = 1
    private val VIEW_TYPE_MESSAGE_RECEIVED = 2

    class ChatHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.chatRecyclerTextView)
    }

    private val diffUtil = object : DiffUtil.ItemCallback<Chat>() {
        override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem == newItem
        }
    }

    private val recyclerListDiffer = AsyncListDiffer(this, diffUtil)
    var chats: List<Chat>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    override fun getItemViewType(position: Int): Int {
        val chat = chats[position]
        return if (chat.userId == FirebaseAuth.getInstance().currentUser?.email.toString()) {
            VIEW_TYPE_MESSAGE_SENT
        } else {
            VIEW_TYPE_MESSAGE_RECEIVED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        val view = if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            LayoutInflater.from(parent.context).inflate(R.layout.chat_recycler_row, parent, false)
        } else {
            LayoutInflater.from(parent.context).inflate(R.layout.chat_recycler_row_right, parent, false)
        }
        return ChatHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ChatHolder, position: Int) {
        val chat = chats[position]
        holder.textView.text = "${chat.userId} : ${chat.text}"
    }

    override fun getItemCount(): Int {
        return chats.size
    }
}
