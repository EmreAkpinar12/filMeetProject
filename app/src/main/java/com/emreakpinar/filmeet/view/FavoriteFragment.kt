package com.emreakpinar.filmeet.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.emreakpinar.filmeet.databinding.FragmentFavoriteBinding
import com.emreakpinar.filmeet.model.FavoriteMovie
import com.emreakpinar.filmeet.view.adapter.FavoriteAdapter
import com.emreakpinar.filmeet.view.adapter.FavoriteClickListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FavoriteFragment : Fragment()  , FavoriteClickListener{
    private var _binding: FragmentFavoriteBinding? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var favoriteList: ArrayList<FavoriteMovie>
    private val db = Firebase.firestore
    private lateinit var adapter: FavoriteAdapter

    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteList = arrayListOf()
        adapter = FavoriteAdapter(favoriteList,this)
        binding.FavoriteRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.FavoriteRecyclerView.adapter = adapter


        fetchFavoriteMovies()
    }

    private fun fetchFavoriteMovies() {
        val userId = firebaseAuth.currentUser?.uid ?: return

        db.collection("users")
            .document(userId)
            .collection("favorites")
            .get()
            .addOnSuccessListener { documents ->
                favoriteList.clear()
                for (document in documents) {
                    val filmId = document.id
                    val movieData = document.data
                    val movie = FavoriteMovie(
                        filmId = filmId.toInt(),
                        overview = movieData["overview"] as? String ?: "",
                        posterPath = movieData["posterPath"] as? String ?: "",
                        timestamp = movieData["timestamp"] as? String ?: "",
                        title = movieData["title"] as? String ?: ""

                    )
                    favoriteList.add(movie)
                }
                if (favoriteList.isEmpty()) {
                    Log.e("FavoriteFragment", "No favorite movies found.")
                } else {
                    adapter.updateFavoriteList(favoriteList)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("FavoriteFragment", "Error getting favorite movies: ", exception)
            }
    }

    override fun onDeleteClicked(movieId: Int) {
        val userId = firebaseAuth.currentUser?.uid ?: return


        db.collection("users")
            .document(userId)
            .collection("favorites")
            .document(movieId.toString())
            .delete()
            .addOnSuccessListener {
                favoriteList = favoriteList.filter { it.filmId != movieId } as ArrayList<FavoriteMovie>
                adapter.updateFavoriteList(favoriteList)
                Log.d("FavoriteFragment", "Movie deleted successfully")
            }
            .addOnFailureListener { exception ->
                Log.e("FavoriteFragment", "Error deleting movie: ", exception)
            }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }






}
