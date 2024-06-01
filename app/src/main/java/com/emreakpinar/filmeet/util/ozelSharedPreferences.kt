package com.emreakpinar.filmeet.util

import android.content.Context
import android.content.SharedPreferences


class ozelSharedPreferences {
    companion object{
        private val TIME ="time"
        private var sharedPreferences : SharedPreferences? =null
        @Volatile
        private var instance : ozelSharedPreferences? = null
        private val lock = Any( )
        operator  fun  invoke(context : Context)= instance ?: synchronized(lock){
            instance ?: ozelSharedPreferencesOlustur(context).also {

                instance=it
            }

        }
private fun ozelSharedPreferencesOlustur(context: Context): ozelSharedPreferences{

    sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
    return ozelSharedPreferences()
}

        fun zamaniKaydet (zaman:Long){


            sharedPreferences?.edit()?.putLong(TIME,zaman)?.apply()
        }

        fun zamaniAl()= sharedPreferences?.getLong(TIME,0)

    }

}