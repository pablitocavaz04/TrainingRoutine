package com.example.trainingroutine_pablocavaz.data.remote

import com.example.trainingroutine_pablocavaz.data.remote.models.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
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
        @Query("filters[user][id]") userId: String
    ): PersonaResponse

    @GET("/api/users/me")
    suspend fun getUserDetails(@Header("Authorization") token: String): UserDetailsResponse

    @GET("/api/personas")
    suspend fun getJugadores(
        @Header("Authorization") token: String,
        @Query("filters[Rol][\$eq]") rol: String = "Jugador",
        @Query("populate") populate: String = "user"
    ): PersonaWithUserResponse

    @GET("/api/entrenamientos")
    suspend fun getEntrenamientos(
        @Header("Authorization") token: String,
        @Query("populate") populate: String = "entreno"
    ): EntrenamientoResponse

    @GET("/api/sesiones")
    suspend fun getSesionesCreadas(
        @Header("Authorization") token: String,
        @Query("filters[entrenador][user][id][\$eq]") entrenadorId: Int,
        @Query("populate") populate: String = "entrenamiento,jugadores,entrenador"
    ): SesionesResponse


    @GET("/api/sesiones")
    suspend fun getSesionesAsignadas(
        @Header("Authorization") token: String,
        @Query("filters[jugadores][user][id][\$eq]") userId: Int,
        @Query("populate") populate: String = "entrenamiento,jugadores,entrenador"
    ): SesionesResponse


    @POST("/api/sesiones")
    suspend fun createSesion(
        @Header("Authorization") token: String,
        @Body request: CrearSesionRequest
    )
}
