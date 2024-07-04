package com.example.bcsprokotlin.module

import android.content.Context
import androidx.room.Room
import com.example.bcsprokotlin.db.dao.SubjectDao
import com.example.bcsprokotlin.db.database.SubjectNameDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SubjectNameDatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): SubjectNameDatabase {
        return Room.databaseBuilder(
            appContext,
            SubjectNameDatabase::class.java,
            "subject_name_database"
        ).build()
    }

    @Provides
    fun provideExamInfoDao(database: SubjectNameDatabase): SubjectDao {
        return database.subjectDao()
    }
}