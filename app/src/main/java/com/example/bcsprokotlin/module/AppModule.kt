package com.example.bcsprokotlin.module

import com.example.bcsprokotlin.util.Converter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideConverter(): Converter {
        return Converter()
    }
}