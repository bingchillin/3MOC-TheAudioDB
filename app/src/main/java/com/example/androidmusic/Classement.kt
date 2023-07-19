package com.example.androidmusic

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class Music(
    val intChartPlace: String,
    val strArtistMBID:String,
    val strArtist: String,
    val strAlbum: String,
    val strTrackThumb: String,
)

data class Album(
    val intChartPlace: String,
    val strArtistMBID:String,
    val strArtist: String,
    val strAlbum: String,
    val strTrackThumb: String,
)

interface RequeteAPI {

    //Mettre des parametres dans l'URL

    @GET("/trending.php") // Modifier l'URL selon le chemin de l'API qui renvoie la liste des utilisateurs
    fun getMusicClassement(
        @Query("country") country: String = "us",
        @Query("type") type: String = "us",
        @Query("country") format: String = "singles",
    ): Deferred<List<Music>>

    fun getAlbumClassement(
        @Query("country") country: String = "us",
        @Query("type") type: String = "us",
        @Query("country") format: String = "albums",
    ): Deferred<List<Album>>
}

object NetworkManagerClassement {

    private val api = Retrofit.Builder()
        .baseUrl("https://theaudiodb.com/api/v1/json/523532/") // Assurez-vous que l'URL de l'API est correcte
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(RequeteAPI::class.java)

    fun getMusicClassement(): Deferred<List<Music>> {
        return api.getMusicClassement()
    }

    fun getAlbumClassement(): Deferred<List<Album>> {
        return api.getAlbumClassement()
    }
}