package com.gdalamin.bcs_pro.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gdalamin.bcs_pro.db.dao.QuestionDao
import com.gdalamin.bcs_pro.model.Question

@Database(entities = [Question::class], version = 1)
abstract class QuestionDataBase : RoomDatabase() {
    abstract fun getQuestionDao(): QuestionDao

}