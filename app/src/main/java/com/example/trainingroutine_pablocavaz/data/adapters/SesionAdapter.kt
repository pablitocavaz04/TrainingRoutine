package com.example.trainingroutine_pablocavaz.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trainingroutine_pablocavaz.R

data class Sesion(val nombre: String, val entrenador: String,val fecha:String)

class SesionAdapter(private val sesiones: List<Sesion>) :
    RecyclerView.Adapter<SesionAdapter.SesionViewHolder>() {

    inner class SesionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTextView: TextView = itemView.findViewById(R.id.textViewNombreSesion)
        val entrenadorTextView: TextView = itemView.findViewById(R.id.textViewEntrenadorSesion)
        val fechaTextView: TextView = itemView.findViewById(R.id.textViewFechaSesion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SesionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sesion, parent, false)
        return SesionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SesionViewHolder, position: Int) {
        val sesion = sesiones[position]
        holder.nombreTextView.text = sesion.nombre
        holder.entrenadorTextView.text = sesion.entrenador
        holder.fechaTextView.text = sesion.fecha
    }

    override fun getItemCount(): Int = sesiones.size
}
