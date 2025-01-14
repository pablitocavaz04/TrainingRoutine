package com.example.trainingroutine_pablocavaz.data.remote.models

data class EntrenamientoResponse(
    val data: List<Entrenamiento>
)

data class Entrenamiento(
    val id: Int,
    val attributes: EntrenamientoAttributes
)

data class EntrenamientoAttributes(
    val nombre: String,
    val descripcion: String,
    val fecha: String,
    val entreno: Entreno?
)

data class Entreno(
    val data: EntrenoData?
)

data class EntrenoData(
    val attributes: EntrenoAttributes
)

data class EntrenoAttributes(
    val name: String?,
    val url: String?,
    val formats: EntrenoFormats?
)

data class EntrenoFormats(
    val small: EntrenoFormatDetails?,
    val medium : EntrenoFormatDetails?
)

data class EntrenoFormatDetails(
    val url: String
)
