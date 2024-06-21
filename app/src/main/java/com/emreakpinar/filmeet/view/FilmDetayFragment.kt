package com.emreakpinar.filmeet.view

import FilmDetailViewModel
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.emreakpinar.filmeet.databinding.FragmentFilmDetayBinding
import com.emreakpinar.filmeet.model.FilmDetayResponse
import com.emreakpinar.filmeet.model.MovieItem
import com.emreakpinar.filmeet.util.loadImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FilmDetayFragment : Fragment() {
    private lateinit var firebaseAuth: FirebaseAuth
    private val db = Firebase.firestore

    private val _favoriteMovies = MutableLiveData<List<MovieItem>>()
    //val favoriteMovies: LiveData<List<MovieItem>> get() = _favoriteMovies

    private var _binding: FragmentFilmDetayBinding? = null
    private val binding get() = _binding!!

    //private val viewModel: FilmDetayViewModel by viewModels()
    private val viewModel by viewModels<FilmDetailViewModel>()
    private val args by navArgs<FilmDetayFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmDetayBinding.inflate(inflater, container, false)
        binding.favoriteBtn.setOnClickListener {
            toggleFavorite()
        }
        binding.goFavorites.setOnClickListener{
            val action = FilmDetayFragmentDirections.actionFilmDetayFragmentToFavoriteFragment()
            findNavController().navigate(action)
        }

        viewModel.getFilmDetay(movieId = args.movieId)
        observeEvents()

        return binding.root
    }


    private fun observeEvents() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBarDetay.isVisible = it
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            binding.textViewErrorDetay.text = it
            binding.textViewErrorDetay.isVisible = true
        }
        viewModel.movieResponse.observe(viewLifecycleOwner) { movie ->
            binding.imageViewDetay.loadImage(movie.backdropPath)
            binding.textViewVotes.text = movie.voteAverage.toString()
            binding.textViewOverView.text = movie.overview
            binding.textViewLanguage.text = movie.spokenLanguages?.first()?.englishName
            binding.textViewStudio.text = movie.productionCompanies?.first()?.name
            binding.textViewCount.text = movie.voteCount.toString()
            binding.filmDetayGroup.isVisible = true

        }
    }

    private fun checkIsFavorite() {
        val movieId = viewModel.movieResponse.value?.id ?: return
        val userId = firebaseAuth.currentUser?.uid ?: return

        // Film favori mi kontrol et
        db.collection("users")
            .document(userId)
            .collection("favorites")
            .document(movieId.toString())
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    binding.favoriteBtn.text = "Remove Favorite"
                } else {
                    binding.favoriteBtn.text = "Add Favorite"
                }
            }
            .addOnFailureListener { e ->
                Log.w("Favorite", "Favori durumu kontrol edilirken hata oluştu", e)
            }
    }

    private fun toggleFavorite() {
        val movie = viewModel.movieResponse.value ?: return
        val userId = firebaseAuth.currentUser?.uid ?: return

        db.collection("users")
            .document(userId)
            .collection("favorites")
            .document(movie?.id.toString())
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    removeFromFavorite(movie.id!!)
                } else {

                    addToFavorite(movie)
                }
            }
            .addOnFailureListener { e ->
                Log.w("Favorite", "Favori durumu değiştirilirken hata oluştu", e)
            }
    }

    private fun addToFavorite(movie:FilmDetayResponse) {
        val userId = firebaseAuth.currentUser?.uid ?: return

        val favoriteMovie = hashMapOf(
            "filmId" to movie.id,
            "title" to movie.title,
            "overview" to movie.overview,
            "posterPath" to movie.posterPath,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("users")
            .document(userId)
            .collection("favorites")
            .document(movie.id.toString())
            .set(favoriteMovie)
            .addOnSuccessListener {
                Toast.makeText(context, "Movie added to favorites!", Toast.LENGTH_SHORT).show()
                Log.d("Favorite", "Film favorilere başarıyla eklendi")
                binding.favoriteBtn.text = "Remove Favorite"
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "The movie could not be added to favorites: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.w("Favorite", "Film favorilere eklenemedi", e)
            }
    }

    private fun removeFromFavorite(filmId: Int) {
        val userId = firebaseAuth.currentUser?.uid ?: return

        db.collection("users")
            .document(userId)
            .collection("favorites")
            .document(filmId.toString())
            .delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Movie removed from favorites!", Toast.LENGTH_SHORT).show()
                Log.d("Favorite", "Film favorilerden başarıyla kaldırıldı")
                binding.favoriteBtn.text = "Add Favorite"
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "The movie could not be removed from favorites: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.w("Favorite", "Film favorilerden kaldırılamadı", e)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
