package com.emreakpinar.filmeet.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.emreakpinar.filmeet.databinding.FragmentProfileBinding
import com.emreakpinar.filmeet.arkadasekle.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        firestore = Firebase.firestore
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadUserProfile()

        binding.saveButton.setOnClickListener {
            saveUserProfile()
        }
    }

    private fun loadUserProfile() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            CoroutineScope(Dispatchers.Main).launch {
                try {
                    val document = firestore.collection("kullanicilar").document(userId).get().await()
                    val user = document.toObject<User>()
                    user?.let {
                        binding.profileEmailText.text = it.email
                        binding.profileUserNameText.text = it.username
                        binding.profileUid.text = it.uid
                       // binding.hakkimda.setText(it.about) // Hakkımda kısmını yükle
                    }
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun saveUserProfile() {
        val aboutText = binding.hakkimda.editableText.toString().ifEmpty { "Henüz bir şey yazılmadı." }
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userProfile = hashMapOf(
                "uid" to currentUser.uid,
                "email" to currentUser.email,
                "username" to binding.profileUserNameText.text.toString(),
                "about" to aboutText
            )

            CoroutineScope(Dispatchers.Main).launch {
                try {
                    firestore.collection("kullanicilar").document(currentUser.uid).set(userProfile).await()
                    Toast.makeText(requireContext(), "Profil güncellendi.", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Toast.makeText(requireContext(), "Kullanıcı oturumu geçersiz.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
