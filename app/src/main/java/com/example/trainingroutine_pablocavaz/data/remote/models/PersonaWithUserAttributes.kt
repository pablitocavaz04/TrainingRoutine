package com.example.trainingroutine_pablocavaz.data.remote.models

data class PersonaWithUserAttributes(
    val Rol: String,
    val user: UserDataWrapper? // Puede ser null
)

data class UserDataWrapper(
    val data: UserAttributesWrapper? // Puede ser null
)

data class UserAttributesWrapper(
    val id: Int,
    val attributes: User
)