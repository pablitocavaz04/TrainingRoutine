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

}
