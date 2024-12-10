package com.example.trainingroutine_pablocavaz.di

import com.example.trainingroutine_pablocavaz.data.remote.ApiService
import com.example.trainingroutine_pablocavaz.data.remote.RetrofitInstance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return RetrofitInstance.api
    }
}
