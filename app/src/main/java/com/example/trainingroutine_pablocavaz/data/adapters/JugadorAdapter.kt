package com.example.trainingroutine_pablocavaz.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trainingroutine_pablocavaz.R

data class Jugador(val nombre: String, val equipo: String)

class JugadorAdapter(private val jugadores: List<Jugador>) :
    RecyclerView.Adapter<JugadorAdapter.JugadorViewHolder>() {

    inner class JugadorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTextView: TextView = itemView.findViewById(R.id.textViewNombreJugador)
        val equipoTextView: TextView = itemView.findViewById(R.id.textViewEquipoJugador)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JugadorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_jugador, parent, false)
        return JugadorViewHolder(view)
    }

    override fun onBindViewHolder(holder: JugadorViewHolder, position: Int) {
        val jugador = jugadores[position]
        holder.nombreTextView.text = jugador.nombre
        holder.equipoTextView.text = jugador.equipo
    }

    override fun getItemCount(): Int = jugadores.size
}