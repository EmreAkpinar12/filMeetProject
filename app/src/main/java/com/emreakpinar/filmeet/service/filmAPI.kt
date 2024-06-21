package com.emreakpinar.filmeet.service

import com.emreakpinar.filmeet.model.FilmDetayResponse
import com.emreakpinar.filmeet.model.Filmler
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface filmAPI {
    @GET("popular")

    suspend fun getFilmList(@Header("Authorization")token : String): Response<Filmler>

    @GET("{movieId}")

    suspend fun  getMovieDetay(@Path("movieId")movieId : String, @Header("Authorization")token: String):Response<FilmDetayResponse>
}