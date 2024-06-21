
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emreakpinar.filmeet.model.FilmDetayResponse
import com.emreakpinar.filmeet.service.ApiClient
import com.emreakpinar.filmeet.util.Constans
import kotlinx.coroutines.launch

class FilmDetailViewModel : ViewModel() {
    val movieResponse: MutableLiveData<FilmDetayResponse> = MutableLiveData()
    val isLoading = MutableLiveData(false)
    val errorMessage: MutableLiveData<String> = MutableLiveData()
    fun getFilmDetay(movieId: Int) {
        isLoading.value = true

        viewModelScope.launch {
            try {
                val response = ApiClient.getClient()
                    .getMovieDetay(movieId = movieId.toString(), token = Constans.BEARER_TOKEN)

                if (response.isSuccessful) {
                    movieResponse.postValue(response.body())
                } else {
                    if (response.message().isNullOrEmpty()) {
                        errorMessage.value = "An unknown error occurred"
                    } else {
                        errorMessage.value = response.message()
                    }
                }
            } catch (e: Exception) {
                errorMessage.value = e.message
            } finally {
                isLoading.value = false
            }
        }
    }
}
