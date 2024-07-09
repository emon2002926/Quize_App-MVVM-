package com.gdalamin.bcs_pro.module

import com.gdalamin.bcs_pro.api.ApiService
import com.gdalamin.bcs_pro.api.RetrofitInstance
import com.gdalamin.bcs_pro.repository.Repository
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