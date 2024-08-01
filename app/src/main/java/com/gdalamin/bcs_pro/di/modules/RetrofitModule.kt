package com.gdalamin.bcs_pro.di.modules

import com.gdalamin.bcs_pro.data.remote.api.ApiService
import com.gdalamin.bcs_pro.data.remote.api.ExamApi
import com.gdalamin.bcs_pro.data.remote.api.QuestionApi
import com.gdalamin.bcs_pro.data.remote.api.RetrofitInstance
import com.gdalamin.bcs_pro.data.remote.repositories.ExamRepository
import com.gdalamin.bcs_pro.data.remote.repositories.QuestionRepository
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
    fun provideQuestionRepository(questionApi: QuestionApi): QuestionRepository {
        return QuestionRepository(questionApi)
    }


    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return RetrofitInstance.api
    }

    @Provides
    @Singleton
    fun provideQuestionApi(): QuestionApi {
        return RetrofitInstance.questionApi
    }


    @Provides
    @Singleton
    fun provideExamRepository(examApi: ExamApi): ExamRepository {
        return ExamRepository(examApi)
    }

    @Provides
    @Singleton
    fun provideExamApi(): ExamApi {
        return RetrofitInstance.examApi
    }

}