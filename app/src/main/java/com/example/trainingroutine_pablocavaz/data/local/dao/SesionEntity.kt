package com.example.trainingroutine_pablocavaz.data.local.dao

import androidx.room.*
import com.example.trainingroutine_pablocavaz.data.local.entities.SesionEntity

@Dao
interface SesionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSesion(sesion: SesionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSesiones(sesiones: List<SesionEntity>)

    @Query("SELECT * FROM sesiones")
    suspend fun getAllSesiones(): List<SesionEntity>

    @Delete
    suspend fun deleteSesion(sesion: SesionEntity)

    @Query("DELETE FROM sesiones")
    suspend fun deleteAllSesiones()
}
