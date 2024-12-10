package com.example.trainingroutine_pablocavaz.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trainingroutine_pablocavaz.R
import com.example.trainingroutine_pablocavaz.data.remote.models.PersonaWithUserData

class JugadorAdapter(private val jugadores: List<PersonaWithUserData>) :
    RecyclerView.Adapter<JugadorAdapter.JugadorViewHolder>() {

    inner class JugadorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTextView: TextView = itemView.findViewById(R.id.textViewNombreJugador)
        val emailTextView: TextView = itemView.findViewById(R.id.textViewEquipoJugador)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JugadorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_jugador, parent, false)
        return JugadorViewHolder(view)
    }

    override fun onBindViewHolder(holder: JugadorViewHolder, position: Int) {
        val jugador = jugadores[position]

        holder.nombreTextView.text = jugador.attributes.Rol

        val email = jugador.attributes.user?.data?.attributes?.email
        holder.emailTextView.text = email ?: "Email no disponible"
    }

    override fun getItemCount(): Int = jugadores.size
}
