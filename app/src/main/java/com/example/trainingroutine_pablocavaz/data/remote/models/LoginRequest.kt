package com.example.trainingroutine_pablocavaz.data.remote.models

data class LoginRequest(
    val identifier: String, // Puede ser el email o username
    val password: String
)
