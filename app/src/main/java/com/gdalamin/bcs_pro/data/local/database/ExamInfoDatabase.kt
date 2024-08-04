package com.gdalamin.bcs_pro.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gdalamin.bcs_pro.data.local.dao.ExamInfoDao
import com.gdalamin.bcs_pro.data.model.LiveExam

@Database(entities = [LiveExam::class], version = 1)
abstract class ExamInfoDatabase : RoomDatabase() {
    abstract fun examInfoDao(): ExamInfoDao
}