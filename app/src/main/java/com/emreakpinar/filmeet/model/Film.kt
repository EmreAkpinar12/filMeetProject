package com.emreakpinar.filmeet.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Film(



    @ColumnInfo("filmID")
    @SerializedName("filmID")
    val id: Long?,

    @ColumnInfo("filmİsim")
    @SerializedName("filmİsim")
    val title: String?,

    @ColumnInfo("yılı")
    @SerializedName("yılı")
    val year: String?,
    @ColumnInfo("suresi")
    @SerializedName("suresi")
    val runtime: String?,
    @ColumnInfo("turu")
    @SerializedName("turu")
    val genres: String?,
    @ColumnInfo("yonetmen")
    @SerializedName("yonetmen")
    val director: String?,
    @ColumnInfo("oyuncular")
    @SerializedName("oyuncular")
    val actors: String?,
    @ColumnInfo("aciklama")
    @SerializedName("aciklama")
    val plot: String?,

    @ColumnInfo("gorsel")
    @SerializedName("gorsel")
    val posterURL: String?


){
    @PrimaryKey(autoGenerate = true)
    var uuid: Int=0
}
