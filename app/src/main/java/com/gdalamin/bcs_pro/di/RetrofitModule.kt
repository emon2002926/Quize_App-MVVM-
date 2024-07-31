package com.gdalamin.bcs_pro.di

import com.gdalamin.bcs_pro.data.remote.api.ApiService
import com.gdalamin.bcs_pro.data.remote.api.RetrofitInstance
import com.gdalamin.bcs_pro.data.repository.Repository
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
    fun provideRepository(apiService: ApiService): Repository {
        return Repository(apiService)
    }

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return RetrofitInstance.api
    }

}