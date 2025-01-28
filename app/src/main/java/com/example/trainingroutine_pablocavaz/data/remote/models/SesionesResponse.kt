package com.example.trainingroutine_pablocavaz.data.remote.models

data class SesionesResponse(
    val data: List<SesionData>
)

data class SesionData(
    val id: Int,
    val attributes: SesionAttributes
)

data class SesionAttributes(
    val nombre: String,
    val estado: Boolean,
    val direccion: String?, // Nueva dirección (opcional)
    val latitud: Double?,  // Nueva latitud
    val longitud: Double?, // Nueva longitud
    val entrenamiento: EntrenamientoSesionWrapper?,
    val jugadores: JugadoresSesionWrapper?,
    val entrenador: PersonaSesionWrapper?,
    val sesionpicture: Sesionpicture?
)

data class Sesionpicture(
    val data: SesionpcitureData?,
)

data class SesionpcitureData(
    val attributes : SesionpictureAttributes,
)

data class  SesionpictureAttributes(
    val name : String?,
    val  url : String?,
    val formats: SesionpictureFormats?
)

data class SesionpictureFormats(
    val small : SesionpictureFormatsDetails?,
    val medium: SesionpictureFormatsDetails?
)

data class SesionpictureFormatsDetails(
    val url: String
)

data class EntrenamientoSesionWrapper(
    val data: EntrenamientoSesionData?
)

data class EntrenamientoSesionData(
    val id: Int,
    val attributes: EntrenamientoSesionAttributes
)

data class EntrenamientoSesionAttributes(
    val nombre: String,
    val descripcion: String
)

data class JugadoresSesionWrapper(
    val data: List<JugadorSesionData>?
)

data class JugadorSesionData(
    val id: Int,
    val attributes: PersonaSesionAttributes
)

data class PersonaSesionWrapper(
    val data: PersonaSesionData?
)

data class PersonaSesionData(
    val id: Int,
    val attributes: PersonaSesionAttributes
)

data class PersonaSesionAttributes(
    val nombre: String,
    val rol: String
)
