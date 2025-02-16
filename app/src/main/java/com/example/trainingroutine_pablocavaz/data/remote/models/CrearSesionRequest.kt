package com.example.trainingroutine_pablocavaz.data.remote.models

data class CrearSesionRequest(
    val data: CrearSesionData
)

data class CrearSesionData(
    val nombre: String,
    val estado: Boolean,
    val entrenamiento: Int,
    val jugadores: List<Int>,
    val entrenador: Int,
    val direccion: String,
    val latitud: Double,
    val longitud: Double,

    )
