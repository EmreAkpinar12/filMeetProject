package com.emreakpinar.filmeet.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.emreakpinar.filmeet.model.Filmler
import com.emreakpinar.filmeet.model.MovieItem
import com.google.firebase.firestore.auth.User
import kotlin.concurrent.Volatile


@Database(entities = [MovieItem::class], version = 1 , exportSchema = false)
abstract class filmDataBase : RoomDatabase(){
    abstract  fun  filmDao():filmDAO

    companion object{
       @Volatile
        private var instance : filmDataBase? = null
        private val lock = Any( )
        operator  fun  invoke(context : Context)= instance ?: synchronized(lock){
           instance ?: filmDataBase.databaseOlustur(context).also {

              instance=it
}

        }

        private fun databaseOlustur (context : Context) = Room.databaseBuilder(

            context.applicationContext,
            filmDataBase::class.java,
            "filmDataBase"

        )
            .build()
    }
}



