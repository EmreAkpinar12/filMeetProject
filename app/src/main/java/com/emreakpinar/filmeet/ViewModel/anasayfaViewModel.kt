package com.emreakpinar.filmeet.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.emreakpinar.filmeet.model.MovieItem
import com.emreakpinar.filmeet.roomdb.filmDataBase
import com.emreakpinar.filmeet.service.ApiClient
import com.emreakpinar.filmeet.service.filmAPIService
import com.emreakpinar.filmeet.util.Constans
import com.emreakpinar.filmeet.util.ozelSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class anasayfaViewModel(application: Application) : AndroidViewModel(application) {


    val filmler = MutableLiveData<List<MovieItem>>()
    val filmHataMesaji = MutableLiveData<Boolean>()
    val filmYukleniyor = MutableLiveData<Boolean>()

    private val filmApiServis = filmAPIService()
    private val OzelSharedPreferences = ozelSharedPreferences(getApplication())

    private val guncellemeZamani = 10 * 60000000000L

    fun getMovieList() {

        filmYukleniyor.value = true

        viewModelScope.launch {

            try {
                val response = ApiClient.getClient().getFilmList(token = Constans.BEARER_TOKEN)

                if (response.isSuccessful) {

                    filmler.postValue(response.body()?.movieItems)
                } else {

                    if (response.message().isNullOrEmpty()) {

                        filmHataMesaji.value = true
                    } else {
                        filmHataMesaji.value = true
                    }
                }
            } catch (e: Exception) {
                filmHataMesaji.value = true


            } finally {
                filmYukleniyor.value = false

            }
        }

    }

    /*  fun refreshData() {

          val kaydedilmeZamani = ozelSharedPreferences.zamaniAl()

          if (kaydedilmeZamani != null && kaydedilmeZamani != 0L && System.nanoTime() - kaydedilmeZamani < guncellemeZamani) {

              verileriRoomdanAl()

          } else {

              verileriInternettenAl()
          }


      }

      fun refreshDataFromInternet() {

          verileriInternettenAl()
      }
      */


    /*private fun verileriRoomdanAl() {
        filmYukleniyor.value = true

        viewModelScope.launch(Dispatchers.IO) {

            val filmListesi = filmDataBase(getApplication()).filmDao().getAllFilm()
            withContext(Dispatchers.Main) {

                filmlerGoster(filmListesi)
            }
        }


    }
    */

    /* private fun verileriInternettenAl() {
         filmYukleniyor.postValue(true)

         viewModelScope.launch(Dispatchers.IO) {

             val filmListesi = filmApiServis.getData()
             filmler.postValue(filmListesi)
             filmYukleniyor.postValue(false)

         }


     }
     */

    private fun filmlerGoster(filmListesi: List<MovieItem>) {

        filmler.value = filmListesi
        filmHataMesaji.value = false
        filmYukleniyor.value = false

    }

    private fun roomaKaydet(filmListesi: List<MovieItem>) {

        viewModelScope.launch {

            val dao = filmDataBase(getApplication()).filmDao()
            dao.deleteAllFilms()


        }

        ozelSharedPreferences.zamaniKaydet(System.nanoTime())
    }

}