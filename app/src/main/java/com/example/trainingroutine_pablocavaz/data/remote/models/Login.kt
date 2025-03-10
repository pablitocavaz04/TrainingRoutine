package com.example.trainingroutine_pablocavaz.data.remote.models

data class LoginRequest(
    val identifier: String,
    val password: String
)

data class LoginResponse(
    val jwt: String,
    val user: User
)

