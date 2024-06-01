package com.emreakpinar.filmeet.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.emreakpinar.filmeet.model.Film
import com.emreakpinar.filmeet.model.Movie
import com.emreakpinar.filmeet.roomdb.filmDataBase
import com.emreakpinar.filmeet.service.filmAPIService
import com.emreakpinar.filmeet.util.ozelSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class anasayfaViewModel(application: Application) : AndroidViewModel(application) {

    val filmler = MutableLiveData<Movie>()
    val filmHataMesaji = MutableLiveData<Boolean>()
    val filmYukleniyor = MutableLiveData<Boolean>()

    private val filmApiServis = filmAPIService()
    private val OzelSharedPreferences = ozelSharedPreferences(getApplication())

    private val guncellemeZamani = 10 * 60000000000L

    fun refreshData() {

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

    private fun verileriRoomdanAl() {
        filmYukleniyor.value = true

        viewModelScope.launch(Dispatchers.IO) {

            val filmListesi = filmDataBase(getApplication()).filmDao().getAllFilm()
            withContext(Dispatchers.Main) {

                filmlerGoster(filmListesi)
            }
        }


    }

    private fun verileriInternettenAl() {
        filmYukleniyor.postValue(true)

        viewModelScope.launch(Dispatchers.IO) {

            val filmListesi = filmApiServis.getData()
            filmler.postValue(filmListesi)
            filmYukleniyor.postValue(false)

        }


    }

    private fun filmlerGoster(filmListesi: List<Film>) {

        //filmler.value = filmListesi
        filmHataMesaji.value = false
        filmYukleniyor.value = false

    }

    private fun roomaKaydet(filmListesi: List<Film>) {

        viewModelScope.launch {

            val dao = filmDataBase(getApplication()).filmDao()
            dao.deleteAllFilms()
            val uuidListesi = dao.insertAll(*filmListesi.toTypedArray())
            var i = 0
            while (i < filmListesi.size) {
                filmListesi[i].uuid = uuidListesi[i].toInt()
                i = i + 1
            }

        }

        ozelSharedPreferences.zamaniKaydet(System.nanoTime())
    }
}