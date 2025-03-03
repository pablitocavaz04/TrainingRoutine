package com.example.trainingroutine_pablocavaz.data.remote.models

data class RegisterResponse(
    val jwt: String,
    val user: User
)


data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String
)
