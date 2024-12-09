package com.example.trainingroutine_pablocavaz.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sesiones")
data class SesionEntity(
    @PrimaryKey val id: Int,
    val nombre: String,
    val estado: Boolean,
    val entrenamientoId: Int,
    val entrenadorId: Int,
    val jugadoresId: List<Int>
)
