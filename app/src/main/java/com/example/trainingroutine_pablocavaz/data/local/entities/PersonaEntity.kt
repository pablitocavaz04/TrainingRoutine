package com.example.trainingroutine_pablocavaz.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "personas")
data class PersonaEntity(
    @PrimaryKey val id: Int,
    val rol: String,
    val usuarioId: Int
)
