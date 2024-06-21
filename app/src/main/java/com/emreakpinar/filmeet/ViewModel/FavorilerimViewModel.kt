import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emreakpinar.filmeet.App
import com.emreakpinar.filmeet.model.FavoriteMovie
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FilmDetayViewModel : ViewModel() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _favoriteMovies = MutableLiveData<List<FavoriteMovie>>()
    val favoriteMovies: LiveData<List<FavoriteMovie>> get() = _favoriteMovies

    // Firestore instance'ını Application sınıfından alın
    private val db: FirebaseFirestore = App.firestore

    fun getFavoriteMovies() {
        val userId = firebaseAuth.currentUser?.uid ?: return

        db.collection("users")
            .document(userId)
            .collection("favorites")
            .get()
            .addOnSuccessListener { documents ->
                val movies = documents.toObjects(FavoriteMovie::class.java)
                _favoriteMovies.postValue(movies)
            }
            .addOnFailureListener { e ->
                Log.w("Favorite", "Favori filmler çekilirken hata oluştu", e)
            }
    }
}

