package com.example.trainingroutine_pablocavaz.data.remote.models

data class UserDetailsResponse(
    val id: Int,
    val username: String,
    val email: String,
    val persona: Persona?
)
