package com.emreakpinar.filmeet.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.emreakpinar.filmeet.arkadasekle.Message
import com.emreakpinar.filmeet.databinding.FragmentChatBinding
import com.emreakpinar.filmeet.view.adapter.ChatAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var adapter: ChatAdapter
    private var messages = arrayListOf<Message>()
    private var friendUid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        friendUid = arguments?.getString("friendUid")

        adapter = ChatAdapter(messages)
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.chatRecyclerView.adapter = adapter

        binding.sendButton.setOnClickListener {
            sendMessage()
        }

        loadMessages()
    }

    private fun loadMessages() {
        val currentUserUid = auth.currentUser?.uid ?: return
        friendUid?.let { friendUid ->
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val documents = firestore.collection("Chats")
                        .document(currentUserUid)
                        .collection(friendUid)
                        .orderBy("timestamp")
                        .get()
                        .await()

                    messages.clear()
                    for (document in documents) {
                        val message = document.toObject(Message::class.java)
                        messages.add(message)
                    }
                    adapter.notifyDataSetChanged()
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    private fun sendMessage() {
        val currentUserUid = auth.currentUser?.uid ?: return
        val messageText = binding.messageEditText.text.toString()

        if (messageText.isNotEmpty()) {
            friendUid?.let { friendUid ->
                val message = Message(
                    senderId = currentUserUid,
                    receiverId = friendUid,
                    message = messageText,
                    timestamp = System.currentTimeMillis()
                )

                CoroutineScope(Dispatchers.Main).launch {
                    try {
                        firestore.collection("Chats")
                            .document(currentUserUid)
                            .collection(friendUid)
                            .add(message)
                            .await()

                        firestore.collection("Chats")
                            .document(friendUid)
                            .collection(currentUserUid)
                            .add(message)
                            .await()

                        binding.messageEditText.text.clear()
                        messages.add(message)
                        adapter.notifyItemInserted(messages.size - 1)
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
