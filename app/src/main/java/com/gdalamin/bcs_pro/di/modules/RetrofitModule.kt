package com.gdalamin.bcs_pro.di.modules

import com.gdalamin.bcs_pro.data.remote.api.ExamApi
import com.gdalamin.bcs_pro.data.remote.api.QuestionApi
import com.gdalamin.bcs_pro.data.remote.api.QuestionBankApi
import com.gdalamin.bcs_pro.data.remote.api.RetrofitInstance
import com.gdalamin.bcs_pro.data.remote.api.SubjectsApi
import com.gdalamin.bcs_pro.data.remote.api.UserNotificationApi
import com.gdalamin.bcs_pro.data.remote.repositories.ExamRepository
import com.gdalamin.bcs_pro.data.remote.repositories.NotificationRepository
import com.gdalamin.bcs_pro.data.remote.repositories.QuestionBankRepository
import com.gdalamin.bcs_pro.data.remote.repositories.QuestionRepository
import com.gdalamin.bcs_pro.data.remote.repositories.SubjectRepository
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
    fun provideQuestionRepository(questionApi: QuestionApi): QuestionRepository {
        return QuestionRepository(questionApi)
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
    
    
    @Provides
    @Singleton
    fun provideQuestionBankRepository(questionBankApi: QuestionBankApi): QuestionBankRepository {
        return QuestionBankRepository(questionBankApi)
    }
    
    @Provides
    @Singleton
    fun provideQuestionBankApi(): QuestionBankApi {
        return RetrofitInstance.questionBankApi
    }
    
    
    @Provides
    @Singleton
    fun provideSubjectsRepository(subjectsApi: SubjectsApi): SubjectRepository {
        return SubjectRepository(subjectsApi)
    }
    
    @Provides
    @Singleton
    fun provideSubjectsApi(): SubjectsApi {
        return RetrofitInstance.subjectsApi
    }
    
    @Provides
    @Singleton
    fun provideNotificationApi(): UserNotificationApi {
        return RetrofitInstance.notificationApi
    }
    
    @Provides
    @Singleton
    fun provideNotificationRepository(notificationApi: UserNotificationApi): NotificationRepository {
        return NotificationRepository(notificationApi)
    }
}