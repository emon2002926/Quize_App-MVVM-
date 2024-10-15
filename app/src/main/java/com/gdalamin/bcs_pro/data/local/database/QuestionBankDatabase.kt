package com.gdalamin.bcs_pro.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gdalamin.bcs_pro.data.local.dao.QuestionBankDao
import com.gdalamin.bcs_pro.data.model.BcsYearName

@Database(entities = [BcsYearName::class], version = 4)
abstract class QuestionBankDatabase : RoomDatabase() {
    abstract fun questionBankDao(): QuestionBankDao
}
