package com.example.trainingroutine_pablocavaz.data.remote.models

data class PersonaRequest(
    val data: PersonaData // Strapi requiere envolver los datos dentro de "data"
)

data class PersonaData(
    val Rol: String,  // El rol del usuario, con mayúscula inicial
    val user: Int     // El ID del usuario
)
