package com.example.trainingroutine_pablocavaz.data.local.dao

import androidx.room.*
import com.example.trainingroutine_pablocavaz.data.local.entities.PersonaEntity

@Dao
interface PersonaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPersona(persona: PersonaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPersonas(personas: List<PersonaEntity>)

    @Query("SELECT * FROM personas")
    suspend fun getAllPersonas(): List<PersonaEntity>

    @Delete
    suspend fun deletePersona(persona: PersonaEntity)

    @Query("DELETE FROM personas")
    suspend fun deleteAllPersonas()
}
