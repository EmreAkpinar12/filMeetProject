package com.emreakpinar.filmeet.service

import com.emreakpinar.filmeet.model.Filmler
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class filmAPIService {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://raw.githubusercontent.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(filmAPI::class.java)

  /*  suspend fun getData(): Filmler {

       // return retrofit.getMovie()
    }
*/
}