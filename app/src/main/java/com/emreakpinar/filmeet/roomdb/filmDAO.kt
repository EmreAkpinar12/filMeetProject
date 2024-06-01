package com.emreakpinar.filmeet.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.emreakpinar.filmeet.model.Film

@Dao
interface filmDAO {


    @Insert
    suspend fun  insertAll (vararg film: Film): List<Long>


    @Query("SELECT * FROM Film WHERE uuid= :filmID")
    suspend fun getFilm(filmID : Int)  : Film


    @Query ("SELECT * FROM Film")
    suspend fun  getAllFilm():    List<Film>

    @Query("DELETE FROM Film")
    suspend fun deleteAllFilms()

}