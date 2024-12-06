package com.example.trainingroutine_pablocavaz.data.remote.models

data class EntrenamientoMinResponse(
    val data: List<EntrenamientoMinData>
)

data class EntrenamientoMinData(
    val id: Int,
    val attributes: EntrenamientoMinAttributes
)

data class EntrenamientoMinAttributes(
    val nombre: String
)
