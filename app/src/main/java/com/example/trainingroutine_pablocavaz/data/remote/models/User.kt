package com.example.trainingroutine_pablocavaz.data.remote.models


data class User(
    val id: String,
    val username: String,
    val email: String,
    val persona: Persona? = null
)
