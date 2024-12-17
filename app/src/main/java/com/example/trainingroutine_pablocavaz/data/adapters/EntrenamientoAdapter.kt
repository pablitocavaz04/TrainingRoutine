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
import com.example.trainingroutine_pablocavaz.data.remote.models.Entrenamiento

class EntrenamientoAdapter(private val entrenamientos: List<Entrenamiento>) :
    RecyclerView.Adapter<EntrenamientoAdapter.EntrenamientoViewHolder>() {

    inner class EntrenamientoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageViewEntrenamiento: ImageView = itemView.findViewById(R.id.imageViewEntrenamiento)
        private val nombreTextView: TextView = itemView.findViewById(R.id.textViewNombreEntrenamiento)
        private val descripcionTextView: TextView = itemView.findViewById(R.id.textViewDescripcionEntrenamiento)
        private val fechaTextView: TextView = itemView.findViewById(R.id.textViewFechaEntrenamiento)

        fun bind(entrenamiento: Entrenamiento) {
            nombreTextView.text = entrenamiento.attributes.nombre
            descripcionTextView.text = entrenamiento.attributes.descripcion
            fechaTextView.text = entrenamiento.attributes.fecha

            val imageUrl = obtenerImagenUrl(entrenamiento)
            imageViewEntrenamiento.load(imageUrl) {
                crossfade(true)
                placeholder(R.drawable.bmba)
            }
        }

        private fun obtenerImagenUrl(entrenamiento: Entrenamiento): String? {
            return entrenamiento.attributes.entreno?.data?.attributes?.formats?.small?.url
                ?: entrenamiento.attributes.entreno?.data?.attributes?.url
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntrenamientoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_entrenamiento, parent, false)
        return EntrenamientoViewHolder(view)
    }

    override fun onBindViewHolder(holder: EntrenamientoViewHolder, position: Int) {
        holder.bind(entrenamientos[position])
    }

    override fun getItemCount(): Int = entrenamientos.size
}
