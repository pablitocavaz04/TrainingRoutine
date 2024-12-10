package com.example.trainingroutine_pablocavaz.data.repository

import com.example.trainingroutine_pablocavaz.data.local.dao.SesionDao
import com.example.trainingroutine_pablocavaz.data.local.entities.SesionEntity
import com.example.trainingroutine_pablocavaz.data.remote.ApiService
import javax.inject.Inject

class SesionRepository @Inject constructor(
    private val sesionDao: SesionDao,
    private val apiService: ApiService
) {

    suspend fun syncSesiones(token: String) {
        val response = apiService.getSesionesCreadas("Bearer $token", entrenadorId = 1)
        val sesionEntities = response.data.map { sesion ->
            val attributes = sesion.attributes
            SesionEntity(
                id = sesion.id,
                nombre = attributes.nombre ?: "Sin Nombre",
                estado = attributes.estado ?: false,
                entrenamientoId = attributes.entrenamiento?.data?.id ?: 0,
                entrenadorId = attributes.entrenador?.data?.id ?: 0,
                jugadoresId = attributes.jugadores?.data?.map { jugador -> jugador.id } ?: emptyList()
            )
        }
        sesionDao.insertSesiones(sesionEntities)
    }



    suspend fun getAllSesiones(): List<SesionEntity> {
        return sesionDao.getAllSesiones()
    }
}
