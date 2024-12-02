package com.example.trainingroutine_pablocavaz.data.remote.models

data class PersonaResponse(
    val data: List<PersonaResponseData>
)

data class PersonaResponseData(
    val id: Int,
    val attributes: PersonaAttributes
)

data class PersonaAttributes(
    val Rol: String
)
