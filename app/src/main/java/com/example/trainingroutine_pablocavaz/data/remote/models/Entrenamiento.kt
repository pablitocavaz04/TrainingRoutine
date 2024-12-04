package com.example.trainingroutine_pablocavaz.data.remote.models

data class Entrenamiento(
    val id: Int,
    val attributes: EntrenamientoAttributes
)

data class EntrenamientoAttributes(
    val nombre: String,
    val descripcion: String,
    val fecha: String
)
