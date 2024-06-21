package com.emreakpinar.filmeet.model




import com.google.gson.annotations.SerializedName



/*@Database(entities = [User::class], version = 1)
@TypeConverters(MovieItemListTypeConventer::class)
abstract class AppDatabase : RoomDatabase(){

    abstract fun userDao(): filmDAO
}
*/


    data class Filmler(
    @SerializedName("results")
    val movieItems: List<MovieItem>,
    )