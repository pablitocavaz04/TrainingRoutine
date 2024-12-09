package com.example.trainingroutine_pablocavaz.di

import android.content.Context
import androidx.room.Room
import com.example.trainingroutine_pablocavaz.data.local.TrainingRoutineDatabase
import com.example.trainingroutine_pablocavaz.data.local.dao.EntrenamientoDao
import com.example.trainingroutine_pablocavaz.data.local.dao.PersonaDao
import com.example.trainingroutine_pablocavaz.data.local.dao.SesionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TrainingRoutineDatabase {
        return Room.databaseBuilder(
            context,
            TrainingRoutineDatabase::class.java,
            "training_routine_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideEntrenamientoDao(database: TrainingRoutineDatabase): EntrenamientoDao {
        return database.entrenamientoDao()
    }

    @Provides
    fun provideSesionDao(database: TrainingRoutineDatabase): SesionDao {
        return database.sesionDao()
    }

    @Provides
    fun providePersonaDao(database: TrainingRoutineDatabase): PersonaDao {
        return database.personaDao()
    }
}
