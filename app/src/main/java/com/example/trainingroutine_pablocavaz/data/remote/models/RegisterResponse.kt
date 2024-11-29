package com.example.trainingroutine_pablocavaz.data.remote.models

data class RegisterResponse(
    val jwt: String,
    val user: User
)

data class User(
    val id: Int,
    val username: String,
    val email: String
)