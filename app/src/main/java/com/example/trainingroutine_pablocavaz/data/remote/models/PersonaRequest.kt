package com.example.trainingroutine_pablocavaz.data.remote.models

data class PersonaRequest(
    val data: PersonaData
)

data class PersonaData(
    val Rol: String,
    val user: Int
)
//Para Crear Una Persona