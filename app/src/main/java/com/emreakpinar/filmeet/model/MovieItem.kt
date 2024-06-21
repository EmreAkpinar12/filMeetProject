package com.emreakpinar.filmeet.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


    @Entity(tableName = "movie")
    data class MovieItem(
    @PrimaryKey(true)
    @SerializedName("id")
    val id: Int =0,
    @SerializedName("overview")
    val overview: String?,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("title")
    val title: String?,
    @SerializedName("vote_average")
    val voteAverage: Double?,
    @SerializedName("vote_count")
    val voteCount: Double?,
    )