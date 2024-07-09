package com.gdalamin.bcs_pro.db.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gdalamin.bcs_pro.db.dao.SubjectDao
import com.gdalamin.bcs_pro.model.SubjectName

@Database(entities = [SubjectName::class], version = 1)
abstract class SubjectNameDatabase : RoomDatabase() {
    abstract fun subjectDao(): SubjectDao

    companion object {
        @Volatile
        private var INSTANCE: SubjectNameDatabase? = null

        fun getDatabase(context: Context): SubjectNameDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SubjectNameDatabase::class.java,
                    "subject_name_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}