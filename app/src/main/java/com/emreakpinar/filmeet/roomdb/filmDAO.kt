package com.emreakpinar.filmeet.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.emreakpinar.filmeet.model.Filmler
import com.emreakpinar.filmeet.model.MovieItem

@Dao
interface filmDAO {


       @Insert
       suspend fun  insertAll (vararg film: MovieItem): List<Long>


      @Query("SELECT * FROM movie WHERE id =:item")
        fun getFilm( item: Int)  : MovieItem


       @Query ("SELECT * FROM movie")
       suspend fun  getAllFilm():    List<MovieItem>

       @Query("DELETE FROM movie")
       suspend fun deleteAllFilms()

}