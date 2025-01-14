package com.example.trainingroutine_pablocavaz.data.repository

import com.example.trainingroutine_pablocavaz.data.local.dao.PersonaDao
import com.example.trainingroutine_pablocavaz.data.local.entities.PersonaEntity
import com.example.trainingroutine_pablocavaz.data.remote.ApiService
import javax.inject.Inject

class PersonaRepository @Inject constructor(
    private val personaDao: PersonaDao,
    private val apiService: ApiService
) {

    suspend fun syncPersonas(token: String) {
        val response = apiService.getJugadores("Bearer $token")
        val personaEntities = response.data.map { persona ->
            val attributes = persona.attributes
            PersonaEntity(
                id = persona.id,
                rol = attributes.Rol ?: "Desconocido",
                usuarioId = attributes.user?.data?.id ?: 0,
                imagen = null
            )
        }
        personaDao.insertPersonas(personaEntities)
    }

    suspend fun getAllPersonas(): List<PersonaEntity> {
        return personaDao.getAllPersonas()
    }
}
