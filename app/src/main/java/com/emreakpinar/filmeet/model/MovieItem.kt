package com.emreakpinar.filmeet.model

data class MovieItem(
    val Casts: String?,
    val Description: String,
    val Director: String?,
    val Genres: String?,
    val Id: Int,
    val Metascore: Int,
    val Rating: Double,
    val RevenueMillions: Double,
    val RuntimeMinutes: Int,
    val Title: String,
    val Votes: Int,
    val Year: Int
)