package com.emreakpinar.filmeet.utils

object FirestoreUtils {
   // private val db = Firebase.firestore
/*
    fun addFavoriteMovie(userId: String, movieTitle: String) {
        val favoriteMovie = FavoriteMovie(title = movieTitle)
        db.collection("users").document(userId).collection("favorites")
            .add(favoriteMovie)
            .addOnSuccessListener {
                Log.d("Firestore", "Favori film başarıyla eklendi")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Favori film eklenirken hata oluştu", e)
            }
    }

    fun fetchUserFavorites(userId: String, callback: (List<String>) -> Unit) {
        db.collection("users").document(userId).collection("favorites")
            .get()
            .addOnSuccessListener { documents ->
                val favoriteMovies = documents.mapNotNull { it.getString("title") }
                callback(favoriteMovies)
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Belgeler alınırken hata oluştu: ", exception)
                callback(emptyList())
            }
    }

    fun calculateMatchRate(list1: List<String>, list2: List<String>): Double {
        val intersection = list1.intersect(list2.toSet()).size
        val union = list1.union(list2.toSet()).size
        return if (union == 0) 0.0 else intersection.toDouble() / union.toDouble()
    }

    fun matchUsers(currentUserId: String, callback: (List<Match>) -> Unit) {
        db.collection("users")
            .get()
            .addOnSuccessListener { documents ->
                val users = documents.mapNotNull { it.toObject(User::class.java) }
                val currentUser = users.find { it.userId == currentUserId }

                if (currentUser != null) {
                    val currentUserFavorites = currentUser.favoriteMovies

                    val matches = users.filter { it.userId != currentUserId }
                        .map { user ->
                            val matchRate = calculateMatchRate(currentUserFavorites, user.favoriteMovies)
                            Match(user.userId, matchRate)
                        }
                        .sortedByDescending { it.matchRate }

                    callback(matches)
                } else {
                    Log.e("Firestore", "Mevcut kullanıcı bulunamadı")
                    callback(emptyList())
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Belgeler alınırken hata oluştu: ", exception)
                callback(emptyList())
            }
    }

    fun fetchFavoriteMovieMatches(userId: String, callback: (List<FavoriteMovieMatch>) -> Unit) {
        fetchUserFavorites(userId) { favorites ->
            val favoriteMovieMatches = favorites.map { FavoriteMovieMatch(title = it) }
            callback(favoriteMovieMatches)
        }
    }

 */
}
