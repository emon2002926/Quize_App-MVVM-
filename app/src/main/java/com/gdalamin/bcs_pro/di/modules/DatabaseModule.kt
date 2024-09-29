package com.gdalamin.bcs_pro.di.modules

import android.content.Context
import androidx.room.Room
import com.gdalamin.bcs_pro.data.local.dao.ExamInfoDao
import com.gdalamin.bcs_pro.data.local.dao.QuestionBankDao
import com.gdalamin.bcs_pro.data.local.dao.SubjectDao
import com.gdalamin.bcs_pro.data.local.database.ExamInfoDatabase
import com.gdalamin.bcs_pro.data.local.database.QuestionBankDatabase
import com.gdalamin.bcs_pro.data.local.database.SubjectNameDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    
    
    @Provides
    @Singleton
    fun provideExamInfoDatabase(@ApplicationContext appContext: Context): ExamInfoDatabase {
        return Room.databaseBuilder(
            appContext,
            ExamInfoDatabase::class.java,
            "exam_info_database"
        ).fallbackToDestructiveMigration()
            .build()
    }
    
    
    @Provides
    fun provideExamInfoDao(database: ExamInfoDatabase): ExamInfoDao {
        return database.examInfoDao()
    }
    
    //
    @Provides
    @Singleton
    fun provideSubjectDatabase(@ApplicationContext appContext: Context): SubjectNameDatabase {
        return Room.databaseBuilder(
            appContext,
            SubjectNameDatabase::class.java,
            "subject_name_database"
        ).build()
    }
    
    @Provides
    fun provideSubjectDao(database: SubjectNameDatabase): SubjectDao {
        return database.subjectDao()
    }
    
    
    @Provides
    @Singleton
    fun provideQuestionBankDatabase(@ApplicationContext appContext: Context): QuestionBankDatabase {
        return Room.databaseBuilder(
            appContext,
            QuestionBankDatabase::class.java,
            "bcs_year_name_database"
        ).build()
    }
    
    @Provides
    fun provideQuestionDao(database: QuestionBankDatabase): QuestionBankDao {
        return database.questionBankDao()
    }
    
    
}