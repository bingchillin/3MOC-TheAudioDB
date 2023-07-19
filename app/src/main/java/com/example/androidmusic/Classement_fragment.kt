package com.example.androidmusic

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ClassementListFragment : Fragment(){

    private val classementAdapter = ClassementsAdapter(object : OnClassementClickListener {
        override fun onProductClicked(user: User) {
            // Ouvrir l'écran
            //findNavController().navigate(ProductsListFragmentDirections.actionProductsListFragmentToProductFragment(product.barcode))
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.list_classement, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = view.findViewById<RecyclerView>(R.id.list_classement)
        list.layoutManager = LinearLayoutManager(requireContext())
        list.adapter = classementsAdapter

        // Appel de l'API pour récupérer les utilisateurs et les ajouter à la liste data
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val classements = NetworkClassementManager.getClassement().await()
                classementsAdapter.setData(classements)
            } catch (e: Exception) {
                // Gérer les erreurs ici
            }
        }
    }
}

class ClassementsAdapter(private val callback: OnClassementClickListener) : RecyclerView.Adapter<ClassementViewHolder>() {
    private val data = mutableListOf<Music>()

    // Cette fonction permet de mettre à jour la liste de données avec de nouveaux utilisateurs
    fun setData(users: List<User>) {
        data.clear()
        data.addAll(users)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.users_list_cell, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        Log.d("ESGI", position.toString())
        val user = data[position]
        holder.update(user)
        holder.itemView.setOnClickListener {
            callback.onProductClicked(user)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}