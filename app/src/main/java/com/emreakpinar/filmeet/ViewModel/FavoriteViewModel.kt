package com.emreakpinar.filmeet.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emreakpinar.filmeet.model.FavoriteMovie
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FavoriteViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val firebaseAuth = FirebaseAuth.getInstance()

    private val _favoriteMovies = MutableLiveData<List<FavoriteMovie>>()
    val favoriteMovies: LiveData<List<FavoriteMovie>> get() = _favoriteMovies


   /* fun fetchFavoriteMovie(userId: String, movieId: String) {
        db.collection("users")
            .document(userId)
            .collection("favorites")
            .document(movieId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val movie = document.toObject(FavoriteMovie::class.java)
                    movie?.let {
                        _favoriteMovies.value = listOf(it)
                    }
                } else {
                    Log.e("FavoriteViewModel", "Favorite movie not found.")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("FavoriteViewModel", "Error getting favorite movie: ", exception)
            }
    }

    */


    /*fun deleteFavoriteMovie(userId: String, movieId: String) {
        firebaseAuth.currentUser?.let { user ->
            db.collection("users")
                .document(user.uid)
                .collection("favorites")
                .document(movieId)
                .delete()
                .addOnSuccessListener {
                    _favoriteMovies.value = _favoriteMovies.value?.filter { it.filmId.toString() != movieId }
                    Log.d("FavoriteViewModel", "Movie deleted successfully")
                }
                .addOnFailureListener { exception ->
                    Log.e("FavoriteViewModel", "Error deleting movie: ", exception)
                }
        }
    }

     */
}


