package com.example.trainingroutine_pablocavaz.data.local.dao

import androidx.room.*
import com.example.trainingroutine_pablocavaz.data.local.entities.EntrenamientoEntity

@Dao
interface EntrenamientoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntrenamiento(entrenamiento: EntrenamientoEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntrenamientos(entrenamientos: List<EntrenamientoEntity>)

    @Query("SELECT * FROM entrenamientos")
    suspend fun getAllEntrenamientos(): List<EntrenamientoEntity>

    @Delete
    suspend fun deleteEntrenamiento(entrenamiento: EntrenamientoEntity)

    @Query("DELETE FROM entrenamientos")
    suspend fun deleteAllEntrenamientos()
}
