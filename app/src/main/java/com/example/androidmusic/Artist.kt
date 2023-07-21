package com.example.androidmusic

import android.os.Parcelable
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import kotlinx.parcelize.Parcelize
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class repAPI(
    val artists: List<Artist>
)

@Parcelize
data class Artist(
    val idArtist:String,
    val strArtist:String,
    val strBiographyFR:String,
    val strCountry:String,
    val strGenre:String,
    val strArtistThumb:String,
) : Parcelable

interface RequeteArtistAPI {
    //Mettre des parametres dans l'URL
    @GET("artist.php")
    fun getArtist(
        @Query("i") i: String
    ): Deferred<repAPI>
}

object NetworkArtistManager {
    private val api = Retrofit.Builder()
        .baseUrl("https://theaudiodb.com/api/v1/json/523532/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(RequeteArtistAPI::class.java)

    fun getArtist(i: String): Deferred<repAPI> {
        return api.getArtist(i)
    }

}