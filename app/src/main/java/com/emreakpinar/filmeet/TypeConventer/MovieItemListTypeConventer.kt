package com.emreakpinar.filmeet.TypeConventer


import androidx.room.TypeConverter
import com.emreakpinar.filmeet.model.MovieItem
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

class MovieItemListTypeConventer  {

    @TypeConverter
    fun fromColumnValue(value: String): List<MovieItem> {
        return Gson().fromJson(value, object : TypeToken<List<MovieItem>>() {}.type)
    }

    @TypeConverter
    fun toColumnValue(list: List<MovieItem>): String {
        return Gson().toJson(list)
    }
}