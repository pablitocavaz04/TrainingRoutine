package com.example.trainingroutine_pablocavaz.data.remote.models

data class LoginResponse(
    val jwt: String,
    val user: User
)
