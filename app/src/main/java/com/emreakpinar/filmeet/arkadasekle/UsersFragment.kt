package com.emreakpinar.filmeet.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.emreakpinar.filmeet.R
import com.emreakpinar.filmeet.databinding.FragmentUsersBinding
import com.emreakpinar.filmeet.arkadasekle.User
import com.emreakpinar.filmeet.view.adapter.UsersAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UsersFragment : Fragment() {
    private var _binding: FragmentUsersBinding? = null
    private val binding get() = _binding!!
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: UsersAdapter
    private var users = arrayListOf<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        adapter = UsersAdapter(users) { user -> addFriend(user) }
        binding.usersRecyclerView.adapter = adapter
        binding.usersRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.myFriends.setOnClickListener{
            findNavController().navigate(R.id.action_usersFragment_to_friendsFragment)
        }

        fetchUsers()
    }

    private fun fetchUsers() {
        firestore.collection("kullanicilar").get().addOnSuccessListener { documents ->
            users.clear()
            for (document in documents) {
                val user = document.toObject(User::class.java).apply {
                    uid = document.id // Belge ID'sini user.uid olarak ayarla
                }
                if (user.uid != auth.currentUser?.uid) {
                    users.add(user)
                }
            }
            adapter.notifyDataSetChanged()
        }.addOnFailureListener { exception ->
            Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

    private fun addFriend(user: User) {
        val currentUserUid = auth.currentUser?.uid ?: return
        val friendRef = firestore.collection("Users").
        document(currentUserUid).collection("Friends")
            .document(user.uid)
        friendRef.set(user).addOnSuccessListener {
            Toast.makeText(requireContext(),
                "${user.username} added as a friend", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
