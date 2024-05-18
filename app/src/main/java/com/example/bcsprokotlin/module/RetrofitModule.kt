package com.example.bcsprokotlin.module

import com.example.bcsprokotlin.api.QuestionApi
import com.example.bcsprokotlin.api.RetrofitInstance
import com.example.bcsprokotlin.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    @Provides
    @Singleton
    fun provideRepository(apiService: QuestionApi): Repository {
        return Repository(apiService)
    }

    @Provides
    @Singleton
    fun provideApiService(): QuestionApi {
        return RetrofitInstance.api
    }

}