package com.gdalamin.bcs_pro.module

import com.gdalamin.bcs_pro.util.GeneralUtils
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
    fun provideConverter(): GeneralUtils {
        return GeneralUtils
    }
}