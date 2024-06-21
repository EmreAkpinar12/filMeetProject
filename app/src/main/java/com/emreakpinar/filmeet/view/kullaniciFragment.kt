package com.emreakpinar.filmeet.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.emreakpinar.filmeet.databinding.FragmentKullaniciBinding
import com.emreakpinar.filmeet.arkadasekle.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class kullaniciFragment : Fragment() {
    private var _binding: FragmentKullaniciBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        firestore = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKullaniciBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.kayitButton.setOnClickListener { kayitOl(it) }
        binding.girisButton.setOnClickListener { girisYap(it) }
        binding.forgotPasswordButton?.setOnClickListener { forgotPassword(it) }

        val guncelKullanici = auth.currentUser
        if (guncelKullanici != null) {
            val action = kullaniciFragmentDirections.actionKullaniciFragmentToAnasayfaFragment()
            Navigation.findNavController(view).navigate(action)
        }
    }

    private fun kayitOl(view: View) {
        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = User(auth.currentUser?.uid ?: "", email)
                    firestore.collection("kullanicilar").document(user.uid).set(user)
                        .addOnSuccessListener {
                            val action = kullaniciFragmentDirections.actionKullaniciFragmentToUserNameFragment()
                            Navigation.findNavController(view).navigate(action)
                        }.addOnFailureListener { e ->
                            Toast.makeText(requireContext(), e.localizedMessage, Toast.LENGTH_LONG).show()
                        }
                } else {
                    task.exception?.let {
                        Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun girisYap(view: View) {
        val email = binding.emailText.text.toString()
        val password = binding.passwordText.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                val action = kullaniciFragmentDirections.actionKullaniciFragmentToAnasayfaFragment()
                Navigation.findNavController(view).navigate(action)
            }.addOnFailureListener { exception ->
                Toast.makeText(requireContext(), exception.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun forgotPassword(view: View) {
        val action = kullaniciFragmentDirections.actionKullaniciFragmentToForgotPasswordFragment()
        Navigation.findNavController(view).navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
