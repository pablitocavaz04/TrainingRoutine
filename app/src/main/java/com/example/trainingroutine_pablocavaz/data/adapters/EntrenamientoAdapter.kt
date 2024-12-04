package com.example.trainingroutine_pablocavaz.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trainingroutine_pablocavaz.R
import com.example.trainingroutine_pablocavaz.data.remote.models.Entrenamiento

class EntrenamientoAdapter(private val entrenamientos: List<Entrenamiento>) :
    RecyclerView.Adapter<EntrenamientoAdapter.EntrenamientoViewHolder>() {

    inner class EntrenamientoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTextView: TextView = itemView.findViewById(R.id.textViewNombreEntrenamiento)
        val descripcionTextView: TextView = itemView.findViewById(R.id.textViewDescripcionEntrenamiento)
        val fechaTextView: TextView = itemView.findViewById(R.id.textViewFechaEntrenamiento)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntrenamientoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_entrenamiento, parent, false)
        return EntrenamientoViewHolder(view)
    }

    override fun onBindViewHolder(holder: EntrenamientoViewHolder, position: Int) {
        val entrenamiento = entrenamientos[position]
        holder.nombreTextView.text = entrenamiento.attributes.nombre
        holder.descripcionTextView.text = entrenamiento.attributes.descripcion
        holder.fechaTextView.text = entrenamiento.attributes.fecha
    }

    override fun getItemCount(): Int = entrenamientos.size
}
