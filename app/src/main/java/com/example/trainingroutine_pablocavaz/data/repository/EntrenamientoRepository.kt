package com.example.trainingroutine_pablocavaz.data.repository

import com.example.trainingroutine_pablocavaz.data.local.dao.EntrenamientoDao
import com.example.trainingroutine_pablocavaz.data.local.entities.EntrenamientoEntity
import com.example.trainingroutine_pablocavaz.data.remote.ApiService
import javax.inject.Inject

class EntrenamientoRepository @Inject constructor(
    private val entrenamientoDao: EntrenamientoDao,
    private val apiService: ApiService
) {

    suspend fun syncEntrenamientos(token: String) {
        val response = apiService.getEntrenamientos("Bearer $token")
        val entrenamientoEntities = response.data.map { entrenamiento ->
            val attributes = entrenamiento.attributes
            EntrenamientoEntity(
                id = entrenamiento.id,
                nombre = attributes.nombre ?: "Sin Nombre",
                descripcion = attributes.descripcion ?: "Sin Descripción",
                fecha = attributes.fecha ?: "Desconocida",
                imagen = null
            )
        }
        entrenamientoDao.insertEntrenamientos(entrenamientoEntities)
    }

    suspend fun getAllEntrenamientos(): List<EntrenamientoEntity> {
        return entrenamientoDao.getAllEntrenamientos()
    }
}
