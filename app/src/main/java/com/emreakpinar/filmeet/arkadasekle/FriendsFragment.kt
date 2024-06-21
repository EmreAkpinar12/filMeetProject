package com.emreakpinar.filmeet.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.emreakpinar.filmeet.arkadasekle.User
import com.emreakpinar.filmeet.databinding.FragmentFriendsBinding

import com.emreakpinar.filmeet.view.adapter.FriendRecyclerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FriendsFragment : Fragment() {
    private var _binding: FragmentFriendsBinding? = null
    private val binding get() = _binding!!
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter: FriendRecyclerAdapter
    private var friendList = arrayListOf<User>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFriendsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        adapter = FriendRecyclerAdapter(friendList, { friend -> deleteFriend(friend) }, { friend -> openChat(friend) })
        binding.friendRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.friendRecyclerView.adapter = adapter

        loadFriends()
    }

    private fun loadFriends() {
        val currentUserUid = auth.currentUser?.uid ?: return
        firestore.collection("Users")
            .document(currentUserUid)
            .collection("Friends")
            .get()
            .addOnSuccessListener { documents ->
                friendList.clear()
                for (document in documents) {
                    val friend = document.toObject(User::class.java)
                    // uid değerini manuel olarak kontrol ve set etme
                    if (friend.uid.isEmpty()) {
                        friend.uid = document.id
                    }
                    friendList.add(friend)
                }
                adapter.updateFriends(friendList)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to load friends", Toast.LENGTH_SHORT).show()
            }
    }

    private fun deleteFriend(friend: User) {
        val currentUserUid = auth.currentUser?.uid ?: return
        firestore.collection("Users")
            .document(currentUserUid)
            .collection("Friends")
            .document(friend.uid)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "${friend.username} removed from friends", Toast.LENGTH_LONG).show()
                friendList.remove(friend)
                adapter.updateFriends(friendList)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to remove friend", Toast.LENGTH_SHORT).show()
            }
    }

    private fun openChat(friend: User) {
        val action = FriendsFragmentDirections.actionFriendsFragmentToChatFragment(friend.uid)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
