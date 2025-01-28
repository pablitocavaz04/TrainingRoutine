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
import com.example.trainingroutine_pablocavaz.data.remote.models.SesionAttributes

class SesionAdapter(private val sesiones: List<SesionAttributes>) :
    RecyclerView.Adapter<SesionAdapter.SesionViewHolder>() {

    inner class SesionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTextView: TextView = itemView.findViewById(R.id.textViewNombreSesion)
        val estadoTextView: TextView = itemView.findViewById(R.id.textViewEstadoSesion)
        val imageViewSesion: ImageView = itemView.findViewById(R.id.imageViewSesion)

        fun bind(sesion: SesionAttributes) {
            nombreTextView.text = sesion.nombre
            estadoTextView.text = if (sesion.estado) "Activa" else "Inactiva"

            // Cargar la imagen de la sesión
            val imageUrl = obtenerImagenUrl(sesion)
            imageViewSesion.load(imageUrl) {
                crossfade(true)
                placeholder(R.drawable.bmba) // Imagen predeterminada mientras carga
            }
        }

        private fun obtenerImagenUrl(sesion: SesionAttributes): String? {
            return sesion.sesionpicture?.data?.attributes?.formats?.small?.url
                ?: sesion.sesionpicture?.data?.attributes?.url
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SesionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sesion, parent, false)
        return SesionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SesionViewHolder, position: Int) {
        holder.bind(sesiones[position])
    }

    override fun getItemCount(): Int = sesiones.size
}
