package com.example.trainingroutine_pablocavaz.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "entrenamientos")
data class EntrenamientoEntity(
    @PrimaryKey val id: Int,
    val nombre: String,
    val descripcion: String,
    val fecha: String,
    val imagen: String? = null
)
