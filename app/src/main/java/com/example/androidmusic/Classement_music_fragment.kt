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

class MusicListFragment : Fragment(){

    private val classementAdapter = MusicAdapter(object : OnMusicClickListener {
        override fun onMusicClicked(music: Music) {
            // Ouvrir l'écran
            findNavController().navigate(ClassementFragmentDirections.actionClassementFragmentToArtistFragment(music.idArtist))
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_music, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = view.findViewById<RecyclerView>(R.id.list_music)
        list.layoutManager = LinearLayoutManager(requireContext())
        list.adapter = classementAdapter

        // Appel de l'API pour récupérer les utilisateurs et les ajouter à la liste data
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val classements = NetworkManagerClassement.getMusicClassement().await()
                classementAdapter.setData(classements.trending.sortedBy { it.intChartPlace.toInt() })
            } catch (e: Exception) {
                // Gérer les erreurs ici, par exemple afficher un message d'erreur.
                Log.e("Erreur_API", "Erreur lors de la récupération des classements : ${e.message}")
            }
        }
    }
}

class MusicAdapter(private val callback: OnMusicClickListener) : RecyclerView.Adapter<MusicViewHolder>() {
    private val data = mutableListOf<Music>()

    // Cette fonction permet de mettre à jour la liste de données avec de nouveaux utilisateurs
    fun setData(classementMusic: List<Music>) {
        data.clear()
        data.addAll(classementMusic)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        return MusicViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_classement_cell, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        Log.d("ESGI", position.toString())
        val music = data[position]
        holder.update(music)
        holder.itemView.setOnClickListener {
            callback.onMusicClicked(music)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}

class MusicViewHolder(v: View) : RecyclerView.ViewHolder(v) {

    private val picture: ImageView = v.findViewById(R.id.track_image)
    private val ranking: TextView = v.findViewById(R.id.id_ranking)
    private val title: TextView = v.findViewById(R.id.title_music)
    private val singer: TextView = v.findViewById(R.id.singer)


    fun update(music: Music) {
        Glide.with(itemView).load(music.strTrackThumb).into(picture)
        ranking.text = music.intChartPlace
        title.text = music.strTrack
        singer.text = music.strArtist
    }

}

interface OnMusicClickListener {
    fun onMusicClicked(music: Music)
}