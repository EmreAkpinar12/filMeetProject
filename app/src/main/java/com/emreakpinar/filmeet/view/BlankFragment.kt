package com.emreakpinar.filmeet.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.emreakpinar.filmeet.databinding.FragmentBlankBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class BlankFragment : Fragment() {
    private var _binding: FragmentBlankBinding? = null
    private val binding get() = _binding!!
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: BlankRecyclerAdapter
    private var chats = arrayListOf<Blank>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firestore = Firebase.firestore
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBlankBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BlankRecyclerAdapter()
        binding.mesajRecycler.adapter = adapter
        binding.mesajRecycler.layoutManager = LinearLayoutManager(requireContext())

        binding.sendButton.setOnClickListener {
            auth.currentUser?.let { user ->
                val chatText = binding.editTextText.text.toString()
                val date = FieldValue.serverTimestamp()
                val dataMap = hashMapOf(
                    "text" to chatText,
                    "user" to user.email!!,
                    "date" to date
                )
                firestore.collection("chats")
                    .add(dataMap)
                    .addOnSuccessListener { binding.editTextText.setText("") }
                    .addOnFailureListener { binding.editTextText.setText("") }
            }
        }

        firestore.collection("chats").orderBy("date", Query.Direction.ASCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Toast.makeText(requireContext(), "Error fetching chats: ${error.message}", Toast.LENGTH_LONG).show()
                    return@addSnapshotListener
                }

                value?.let {
                    if (!it.isEmpty) {
                        val documents = it.documents
                        chats.clear()
                        for (document in documents) {
                            val text = document.getString("text") ?: ""
                            val user = document.getString("user") ?: ""
                            val chat = Blank(user, text)
                            chats.add(chat)
                        }
                        adapter.chats = chats
                        adapter.notifyDataSetChanged()
                    }
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
