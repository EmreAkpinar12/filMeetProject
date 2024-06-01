package com.emreakpinar.filmeet.service

import com.emreakpinar.filmeet.model.Movie
import retrofit2.http.GET

interface filmAPI {
    // https://raw.githubusercontent.com/softbreak/IMDB-Top-1000-Json/main/movies.json
    @GET("softbreak/IMDB-Top-1000-Json/main/movies.json")

    suspend fun getFilm(): Movie
}