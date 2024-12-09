package com.example.trainingroutine_pablocavaz.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.trainingroutine_pablocavaz.data.local.dao.EntrenamientoDao
import com.example.trainingroutine_pablocavaz.data.local.dao.PersonaDao
import com.example.trainingroutine_pablocavaz.data.local.dao.SesionDao
import com.example.trainingroutine_pablocavaz.data.local.entities.EntrenamientoEntity
import com.example.trainingroutine_pablocavaz.data.local.entities.PersonaEntity
import com.example.trainingroutine_pablocavaz.data.local.entities.SesionEntity

@Database(
    entities = [EntrenamientoEntity::class, SesionEntity::class, PersonaEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TrainingRoutineDatabase : RoomDatabase() {
    abstract fun entrenamientoDao(): EntrenamientoDao
    abstract fun sesionDao(): SesionDao
    abstract fun personaDao(): PersonaDao
}
