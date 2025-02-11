package com.example.trainingroutine_pablocavaz.data.remote.models

data class PersonaResponse(
    val data: List<PersonaResponseData>
)

data class PersonaResponseData(
    val id: Int,
    val attributes: PersonaAttributes
)

data class PersonaAttributes(
    val Rol: String,
    val user: UserData?,
    val perfil: Perfil? //Imagen de la tabla persona
)

data class Perfil(
    val data: PerfilData?
)

data class PerfilData(
    val attributes: PerfilAttributes
)

data class  PerfilAttributes(
    val name: String?,
    val url: String?,
    val formats: PerfilFormats?
)

data class PerfilFormats(
    val small: PerfilFormatDetails?
)

data class PerfilFormatDetails(
    val url: String?
)

data class UpdatePersonaRequest(
    val data: UpdatePersonaData
)

data class UpdatePersonaData(
    val perfil: Int
)