package com.example.trainingroutine_pablocavaz.data.remote.models

data class UserData(
    val data: UserAttributes?
)

data class UserAttributes(
    val id: Int,
    val attributes: User
)

data class User(
    val id: Int,
    val username: String,
    val email: String,
    val persona: PersonaResponse
)

data class UploadResponse(
    val id: Int,
    val url: String
)

data class UpdatePersonaRequest(
    val data: UpdatePersonaData
)

data class UpdatePersonaData(
    val perfil: Int
)