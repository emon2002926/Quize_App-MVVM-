package com.gdalamin.bcs_pro.di.modules

import android.content.Context
import androidx.room.Room
import com.gdalamin.bcs_pro.data.local.dao.ExamInfoDao
import com.gdalamin.bcs_pro.data.local.database.ExamInfoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExamInfoDatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): ExamInfoDatabase {
        return Room.databaseBuilder(
            appContext,
            ExamInfoDatabase::class.java,
            "exam_info_database"
        ).build()
    }

    @Provides
    fun provideExamInfoDao(database: ExamInfoDatabase): ExamInfoDao {
        return database.examInfoDao()
    }
}