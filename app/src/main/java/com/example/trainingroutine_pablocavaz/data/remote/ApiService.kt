package com.example.trainingroutine_pablocavaz.data.remote

import LoginResponse
import com.example.trainingroutine_pablocavaz.data.remote.models.LoginRequest
import com.example.trainingroutine_pablocavaz.data.remote.models.PersonaRequest
import com.example.trainingroutine_pablocavaz.data.remote.models.RegisterRequest
import com.example.trainingroutine_pablocavaz.data.remote.models.RegisterResponse
import com.example.trainingroutine_pablocavaz.data.remote.models.UserDetailsResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    // Registro de usuario
    @POST("/api/auth/local/register")
    suspend fun register(@Body user: RegisterRequest): RegisterResponse

    // Login de usuario
    @POST("/api/auth/local")
    suspend fun login(@Body credentials: LoginRequest): LoginResponse

    // Obtener detalles de usuario
    @GET("/api/personas/{id}")
    suspend fun getUserDetails(@Path("id") userId: String): UserDetailsResponse

    // Crear persona asociada al usuario
    @POST("/api/personas")
    suspend fun createPersona(
        @Header("Authorization") token: String,
        @Body request: PersonaRequest
    )
}
