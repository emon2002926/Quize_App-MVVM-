package com.gdalamin.bcs_pro.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gdalamin.bcs_pro.data.local.dao.ExamInfoDao
import com.gdalamin.bcs_pro.data.model.LiveExam

@Database(entities = [LiveExam::class], version = 1)
abstract class ExamInfoDatabase : RoomDatabase() {
    abstract fun examInfoDao(): ExamInfoDao

    companion object {
        @Volatile
        private var INSTANCE: ExamInfoDatabase? = null

        fun getDatabase(context: Context): ExamInfoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExamInfoDatabase::class.java,
                    "exam_info_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}