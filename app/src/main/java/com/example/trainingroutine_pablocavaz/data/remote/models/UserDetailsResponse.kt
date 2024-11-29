package com.example.trainingroutine_pablocavaz.data.remote.models

data class UserDetailsResponse(
    val id: String,
    val username: String,
    val email: String,
    val rol: String // "jugador" o "entrenador"
)
