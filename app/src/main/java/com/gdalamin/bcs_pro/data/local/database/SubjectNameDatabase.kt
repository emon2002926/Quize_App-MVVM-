package com.gdalamin.bcs_pro.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gdalamin.bcs_pro.data.local.dao.SubjectDao
import com.gdalamin.bcs_pro.data.model.SubjectName

@Database(entities = [SubjectName::class], version = 1)
abstract class SubjectNameDatabase : RoomDatabase() {
    abstract fun subjectDao(): SubjectDao

}