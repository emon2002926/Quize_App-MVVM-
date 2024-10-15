package com.gdalamin.bcs_pro.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.gdalamin.bcs_pro.data.model.Question

@Dao
interface QuestionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addQuestion(question: List<Question>)
    
    
    @Update
    fun updateQuestion(question: Question)
    
    
    @Query("SELECT * FROM Question")
    fun getQuestion(): List<Question>
    
    // Add this method to search based on the 'batch' column
    @Query("SELECT * FROM Question WHERE batch = :batch")
    fun getQuestionsByBatch(batch: String): List<Question>
    
    
    // Retrieve questions by batch and multiple subjects
    @Query("SELECT * FROM Question WHERE subjects IN (:subjects) AND batch = :batch")
    fun getQuestionsByBatchAndSubjects(batch: String, subjects: List<String>): List<Question>
    
}