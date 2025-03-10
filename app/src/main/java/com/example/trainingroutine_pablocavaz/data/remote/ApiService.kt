package com.example.trainingroutine_pablocavaz.data.remote

import com.example.trainingroutine_pablocavaz.data.remote.models.*
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    @POST("/api/auth/local/register")
    suspend fun register(@Body user: RegisterRequest): RegisterResponse

    @POST("/api/auth/local")
    suspend fun login(@Body credentials: LoginRequest): LoginResponse

    @POST("/api/personas")
    suspend fun createPersona(
        @Header("Authorization") token: String,
        @Body request: PersonaRequest
    )

    @GET("/api/personas")
    suspend fun getPersonasByUserId(
        @Header("Authorization") token: String,
        @Query("filters[user][id]") userId: String,
        @Query("populate") populate: String = "perfil"
    ): PersonaResponse

    @GET("/api/users/me")
    suspend fun getUserDetails(@Header("Authorization") token: String): User

    @GET("/api/personas")
    suspend fun getJugadores(
        @Header("Authorization") token: String,
        @Query("filters[Rol][\$eq]") rol: String = "Jugador",
        @Query("populate") populate: String = "user,perfil"
    ): PersonaResponse

    @GET("/api/entrenamientos")
    suspend fun getEntrenamientos(
        @Header("Authorization") token: String,
        @Query("populate") populate: String = "entreno"
    ): EntrenamientoResponse

    @GET("/api/sesiones")
    suspend fun getSesionesCreadas(
        @Header("Authorization") token: String,
        @Query("filters[entrenador][user][id][\$eq]") entrenadorId: Int,
        @Query("populate") populate: String = "entrenamiento,jugadores,entrenador,sesionpicture"
    ): SesionesResponse


    @GET("/api/sesiones")
    suspend fun getSesionesAsignadas(
        @Header("Authorization") token: String,
        @Query("filters[jugadores][user][id][\$eq]") userId: Int,
        @Query("populate") populate: String = "entrenamiento,jugadores,entrenador,sesionpicture"
    ): SesionesResponse


    @POST("/api/sesiones")
    suspend fun createSesion(
        @Header("Authorization") token: String,
        @Body request: CrearSesionRequest
    )

    @GET("maps/api/geocode/json")
    suspend fun getGeocodingData(
        @Query("address") address: String,
        @Query("key") apiKey: String
    ): GeocodingResponse

    @Multipart
    @POST("/api/upload")
    suspend fun uploadImage(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part
    ): List<UploadResponse>

    @retrofit2.http.PUT("/api/personas/{id}")
    suspend fun updatePersonaProfileImage(
        @Header("Authorization") token: String,
        @retrofit2.http.Path("id") personaId: Int,
        @Body request: UpdatePersonaRequest
    ): PersonaResponse


}
