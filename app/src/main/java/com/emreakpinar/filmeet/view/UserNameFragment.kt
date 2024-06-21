package com.emreakpinar.filmeet.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.emreakpinar.filmeet.databinding.FragmentUserNameBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class UserNameFragment : Fragment() {
    private var _binding: FragmentUserNameBinding? = null
    private val binding get() = _binding!!

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        binding.saveUsernameButton.setOnClickListener {
            saveUsername()
        }

    }

    private fun saveUsername() {
        val username = binding.usernameText.text.toString()
        val currentUserUid = auth.currentUser?.uid

        if (username.isNotEmpty() && currentUserUid != null) {
            val userRef = firestore.collection("kullanicilar").document(currentUserUid)

            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val documentSnapshot = userRef.get().await()
                    if (documentSnapshot.exists()) {
                        userRef.update("username", username).await()
                    } else {
                        val userData = hashMapOf(
                            "uid" to currentUserUid,
                            "username" to username
                        )
                        userRef.set(userData).await()
                    }
                    // Navigate to the next screen on success
                    val action = UserNameFragmentDirections.actionUserNameFragmentToAnasayfaFragment()
                    findNavController().navigate(action)
                } catch (e: Exception) {
                    // Show error message and log it
                    Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_LONG).show()
                    Log.e("FirestoreError", "Error updating or creating username", e)
                }
            }
        } else {
            // Show error message if username is empty or currentUserUid is null
            Toast.makeText(requireContext(), "Username cannot be empty or user is not authenticated", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
