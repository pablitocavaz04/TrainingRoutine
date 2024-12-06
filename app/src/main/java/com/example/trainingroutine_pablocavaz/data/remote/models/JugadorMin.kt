package com.example.trainingroutine_pablocavaz.data.remote.models

data class JugadorMinResponse(
    val data: List<JugadorMinData>
)

data class JugadorMinData(
    val id: Int,
    val attributes: JugadorMinAttributes
)

data class JugadorMinAttributes(
    val nombre: String
)
