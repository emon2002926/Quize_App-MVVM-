package com.gdalamin.bcs_pro.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.gdalamin.bcs_pro.model.Question

@Dao
interface QuestionDao {
    @Upsert
    fun addQuestion(question: Question)

    @Delete
    fun deleteTask(question: Question)


    @Update
    fun updateQuestion(question: Question)

    @Query("SELECT * FROM Question")
    fun getLiveQuestion(): LiveData<List<Question>>


    @Query("SELECT * FROM Question")
    fun getQuestion(): List<Question>

}