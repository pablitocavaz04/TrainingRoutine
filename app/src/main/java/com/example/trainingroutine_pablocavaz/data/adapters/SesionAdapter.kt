package com.example.trainingroutine_pablocavaz.data.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trainingroutine_pablocavaz.R
import com.example.trainingroutine_pablocavaz.data.remote.models.SesionAttributes

class SesionAdapter(private val sesiones: List<SesionAttributes>) :
    RecyclerView.Adapter<SesionAdapter.SesionViewHolder>() {

    inner class SesionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTextView: TextView = itemView.findViewById(R.id.textViewNombreSesion)
        val estadoTextView: TextView = itemView.findViewById(R.id.textViewEstadoSesion)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SesionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_sesion, parent, false)
        return SesionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SesionViewHolder, position: Int) {
        val sesion = sesiones[position]
        println("SESION DATA: $sesion")
        holder.nombreTextView.text = sesion.nombre
        holder.estadoTextView.text = if (sesion.estado) "Activa" else "Inactiva"
    }


    override fun getItemCount(): Int = sesiones.size
}
