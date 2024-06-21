package com.emreakpinar.filmeet.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.emreakpinar.filmeet.R
import com.google.firebase.auth.FirebaseAuth



class BlankRecyclerAdapter : RecyclerView.Adapter<BlankRecyclerAdapter.BlankHolder>() {
    private val VIEW_TYPE_MESSAGE_SENT = 1
    private val VIEW_TYPE_MESSAGE_RECEIVED = 2

    class BlankHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val diffUtil = object : DiffUtil.ItemCallback<Blank>() {
        override fun areItemsTheSame(oldItem: Blank, newItem: Blank): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Blank, newItem: Blank): Boolean {
            return oldItem == newItem
        }
    }
    private val recyclerListDiffer = AsyncListDiffer(this, diffUtil)

    var chats: List<Blank>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    override fun getItemViewType(position: Int): Int {
        val chat = chats[position]
        return if (chat.user == FirebaseAuth.getInstance().currentUser?.email.toString()) {
            VIEW_TYPE_MESSAGE_SENT
        } else {
            VIEW_TYPE_MESSAGE_RECEIVED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlankHolder {
        val view = if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            LayoutInflater.from(parent.context).inflate(R.layout.chat_recycler_row, parent, false)
        } else {
            LayoutInflater.from(parent.context).inflate(R.layout.chat_recycler_row_right, parent, false)
        }
        return BlankHolder(view)
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    override fun onBindViewHolder(holder: BlankHolder, position: Int) {
        val textView = holder.itemView.findViewById<TextView>(R.id.chatRecyclerTextView)
        textView.text = "${chats[position].user}: ${chats[position].text}"
    }
}
