package com.example.trainingroutine_pablocavaz.data.remote.models

data class PersonaWithUserAttributes(
    val Rol: String,
    val user: UserDataWrapper?
)

data class UserDataWrapper(
    val data: UserAttributesWrapper?
)

data class UserAttributesWrapper(
    val id: Int,
    val attributes: User
)