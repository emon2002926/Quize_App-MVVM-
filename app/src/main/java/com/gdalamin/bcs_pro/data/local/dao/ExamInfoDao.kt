package com.gdalamin.bcs_pro.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.gdalamin.bcs_pro.data.model.LiveExam

@Dao
interface ExamInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addExamInfo(examInfo: LiveExam)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(exams: List<LiveExam>)

    @Update
    suspend fun updateExamInfo(examInfo: LiveExam)

    @Query("SELECT * FROM LiveExam")
    fun getExamInfo(): LiveData<List<LiveExam>>

    @Query("SELECT * FROM LiveExam")
    suspend fun getAllExamsNonLive(): List<LiveExam>

    @Query("DELETE FROM LiveExam")
    suspend fun deleteAll()
}