package com.example.trainingroutine_pablocavaz.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.crossfade
import coil3.request.placeholder
import com.example.trainingroutine_pablocavaz.R
import com.example.trainingroutine_pablocavaz.data.remote.models.PersonaResponseData

class JugadorAdapter(private val jugadores: List<PersonaResponseData>) :
    RecyclerView.Adapter<JugadorAdapter.JugadorViewHolder>() {

    inner class JugadorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagenImageView: ImageView = itemView.findViewById(R.id.imagenJugadorImageView)
        val nombreTextView: TextView = itemView.findViewById(R.id.textViewNombreJugador)
        val emailTextView: TextView = itemView.findViewById(R.id.textViewEmailJugador)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JugadorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_jugador, parent, false)
        return JugadorViewHolder(view)
    }

    override fun onBindViewHolder(holder: JugadorViewHolder, position: Int) {
        val jugador = jugadores[position]

        holder.nombreTextView.text = jugador.attributes.Rol
        holder.emailTextView.text = jugador.attributes.user?.data?.attributes?.email ?: "Email no disponible"

        val imageUrl = jugador.attributes.perfil?.data?.attributes?.url
            ?: jugador.attributes.perfil?.data?.attributes?.formats?.thumbnail?.url

        holder.imagenImageView.load(imageUrl) {
            crossfade(true)
            placeholder(R.drawable.bmba)
        }

    }

    override fun getItemCount(): Int = jugadores.size
}
