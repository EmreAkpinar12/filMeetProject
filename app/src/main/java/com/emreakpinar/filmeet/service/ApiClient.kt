package com.emreakpinar.filmeet.service

import com.emreakpinar.filmeet.model.Filmler
import com.emreakpinar.filmeet.util.Constans
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object ApiClient {

    fun getClient(): filmAPI {

        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS).build()

        return Retrofit.Builder().baseUrl(Constans.Base_URL)
            .addConverterFactory(GsonConverterFactory.create()).client(client).build()
            .create(filmAPI::class.java)

    }
}