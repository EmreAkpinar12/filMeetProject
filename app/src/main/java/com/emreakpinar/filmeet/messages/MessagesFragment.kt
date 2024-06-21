package com.emreakpinar.filmeet.messages

import com.emreakpinar.filmeet.adapter.Chat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.emreakpinar.filmeet.databinding.FragmentMessagesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MessagesFragment : Fragment() {
    private var _binding: FragmentMessagesBinding? = null
    private val binding get() = _binding!!

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: ChatRecyclerAdapter
    private var chats = arrayListOf<Chat>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = Firebase.firestore
        auth = Firebase.auth

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessagesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ChatRecyclerAdapter()
        binding.chatRecycler.adapter = adapter
        binding.chatRecycler.layoutManager = LinearLayoutManager(requireContext())


        binding.sendButton.setOnClickListener {
            val user = auth.currentUser!!.email
            val chatText = binding.chatText.text.toString()
            val date = FieldValue.serverTimestamp()

            if (user != null && chatText.isNotEmpty()) {
                val dataMap = HashMap<String, Any>().apply {
                    put("text", chatText)
                    put("date", date)
                    put("user", user)
                }

                firestore.collection("Chats")
                    .add(dataMap)
                    .addOnSuccessListener {
                        binding.chatText.setText("")
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG).show()
                        binding.chatText.setText("")
                    }
            } else {
                Toast.makeText(requireContext(), "Message cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        firestore.collection("Chats").orderBy("date", Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Toast.makeText(requireContext(), error.localizedMessage, Toast.LENGTH_LONG).show()
                } else {
                    value?.let {
                        if (it.isEmpty) {
                            Toast.makeText(requireContext(), "No messages", Toast.LENGTH_LONG).show()
                        } else {
                            val documents = it.documents

                            chats.clear()
                            for (document in documents) {
                                val text = document.getString("text") ?: ""
                                val user = document.getString("user") ?: ""
                                val chat = Chat(user, text)
                                chats.add(chat)
                            }
                            adapter.chats = chats
                            adapter.notifyDataSetChanged()
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
