package com.example.androidmusic

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ArtistFragment : Fragment() {

    private val model: ArtistViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(requireContext()).inflate(R.layout.artist_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = ArtistFragmentArgs.fromBundle(requireArguments()).id
        model.findArtist(id)

        lifecycleScope.launch {
            model.artistFlow.collect { response ->
                if (response != null) {

                    val artist = response.artists[0]

                    Glide.with(this@ArtistFragment).load(artist.strArtistThumb)
                        .into(view.findViewById<ImageView>(R.id.image_product))
                    view.findViewById<TextView>(R.id.artist_name).text = artist.strArtist
                    view.findViewById<TextView>(R.id.city_genre).text = artist.strCountry + " - " + artist.strGenre
                    view.findViewById<TextView>(R.id.description).text = artist.strBiographyFR
                }
            }

        }
    }
}

class ArtistViewModel : ViewModel() {
    val artistFlow = MutableStateFlow<repAPI?>(null)

    fun findArtist(i: String) {
        viewModelScope.launch {
            try {
                val artist = NetworkArtistManager.getArtist(i).await()
                artistFlow.emit(artist)
            } catch (e: Exception) {
                artistFlow.emit(null)
            }
        }
    }
}