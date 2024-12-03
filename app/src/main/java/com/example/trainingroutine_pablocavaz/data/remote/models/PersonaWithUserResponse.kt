package com.example.trainingroutine_pablocavaz.data.remote.models

data class PersonaWithUserResponse(
    val data: List<PersonaWithUserData>
)

data class PersonaWithUserData(
    val id: Int,
    val attributes: PersonaWithUserAttributes
)
