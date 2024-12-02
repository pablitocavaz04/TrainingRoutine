package com.example.trainingroutine_pablocavaz.data.remote

import com.example.trainingroutine_pablocavaz.data.remote.models.LoginRequest
import com.example.trainingroutine_pablocavaz.data.remote.models.LoginResponse
import com.example.trainingroutine_pablocavaz.data.remote.models.PersonaRequest
import com.example.trainingroutine_pablocavaz.data.remote.models.RegisterRequest
import com.example.trainingroutine_pablocavaz.data.remote.models.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    // Registro de usuario
    @POST("/api/auth/local/register")
    suspend fun register(@Body user: RegisterRequest): RegisterResponse

    // Login de usuario
    @POST("/api/auth/local")
    suspend fun login(@Body credentials: LoginRequest): LoginResponse

    // Crear la persona del usuario que hemos creado
    @POST("/api/personas")
    suspend fun createPersona(
        @Header("Authorization") token: String,
        @Body request: PersonaRequest
    )
}
