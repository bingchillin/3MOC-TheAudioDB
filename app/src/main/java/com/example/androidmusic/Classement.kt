package com.example.androidmusic

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class ClassementMusics(
    val trending: List<Music>,
)
data class ClassementAlbums(
    val trending: List<Album>,
)

data class Music(
    val idAlbum:String,
    val idArtist: String,
    val intChartPlace: String,
    val strArtistMBID:String,
    val strAlbumMBID:String,
    val strArtist: String,
    val strAlbum: String,
    val strTrack: String,
    val strTrackThumb: String,
)

data class Album(
    val idAlbum:String,
    val idArtist: String,
    val intChartPlace: String,
    val strArtistMBID:String,
    val strAlbumMBID:String,
    val strArtist: String,
    val strAlbum: String,
    val strAlbumThumb: String,
)

interface RequeteAPI {

    //Mettre des parametres dans l'URL
    @GET("trending.php")
    fun getMusicClassement(
        @Query("country") country: String = "us",
        @Query("type") type: String = "itunes",
        @Query("format") format: String = "singles",
    ): Deferred<ClassementMusics>

    @GET("trending.php")
    fun getAlbumClassement(
        @Query("country") country: String = "us",
        @Query("type") type: String = "itunes",
        @Query("format") formatAlbum: String = "albums", // Change the name to formatAlbum
    ): Deferred<ClassementAlbums>
}

object NetworkManagerClassement {

    private val api = Retrofit.Builder()
        .baseUrl("https://theaudiodb.com/api/v1/json/523532/") // Assurez-vous que l'URL de l'API est correcte
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(RequeteAPI::class.java)

    fun getMusicClassement(): Deferred<ClassementMusics> {
        return api.getMusicClassement()
    }

    fun getAlbumClassement(): Deferred<ClassementAlbums> {
        return api.getAlbumClassement()
    }
}