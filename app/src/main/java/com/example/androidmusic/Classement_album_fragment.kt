package com.example.androidmusic

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AlbumListFragment : Fragment(){

    private val classementAdapter = AlbumAdapter(object : OnAlbumClickListener {
        override fun onAlbumClicked(album: Album) {
            // Ouvrir l'écran
            findNavController().navigate(ClassementFragmentDirections.actionClassementFragmentToArtistFragment(album.idArtist))
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_album, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = view.findViewById<RecyclerView>(R.id.list_album)
        list.layoutManager = LinearLayoutManager(requireContext())
        list.adapter = classementAdapter

        // Appel de l'API pour récupérer les utilisateurs et les ajouter à la liste data
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val classements = NetworkManagerClassement.getAlbumClassement().await()
                classementAdapter.setData(classements.trending.sortedBy { it.intChartPlace.toInt() })
            } catch (e: Exception) {
                // Gérer les erreurs ici, par exemple afficher un message d'erreur.
                Log.e("Erreur_API", "Erreur lors de la récupération des classements : ${e.message}")
            }
        }
    }
}

class AlbumAdapter(private val callback: OnAlbumClickListener) : RecyclerView.Adapter<AlbumViewHolder>() {
    private val data = mutableListOf<Album>()

    // Cette fonction permet de mettre à jour la liste de données avec de nouveaux utilisateurs
    fun setData(classementAlbum: List<Album>) {
        data.clear()
        data.addAll(classementAlbum)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return AlbumViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_classement_cell, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        Log.d("ESGI", position.toString())
        val album = data[position]
        holder.update(album)
        holder.itemView.setOnClickListener {
            callback.onAlbumClicked(album)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

class AlbumViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    private val picture: ImageView = v.findViewById(R.id.track_image)
    private val ranking: TextView = v.findViewById(R.id.id_ranking)
    private val title: TextView = v.findViewById(R.id.title_music)
    private val singer: TextView = v.findViewById(R.id.singer)


    fun update(album: Album) {
        Glide.with(itemView).load(album.strAlbumThumb).into(picture)
        ranking.text = album.intChartPlace
        title.text = album.strAlbum
        singer.text = album.strArtist
    }

}

interface OnAlbumClickListener {
    fun onAlbumClicked(album: Album)
}