package com.example.bcsprokotlin.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bcsprokotlin.db.dao.QuestionDao
import com.example.bcsprokotlin.model.Question

@Database(entities = [Question::class], version = 1)
abstract class QuestionDataBase : RoomDatabase() {
    abstract fun getQuestionDao(): QuestionDao

}