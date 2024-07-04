package com.example.bcsprokotlin.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.bcsprokotlin.model.SubjectName

@Dao
interface SubjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSubjectName(subject: SubjectName)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSubjectName(exams: List<SubjectName>)

    @Update
    suspend fun updateSubjectName(examInfo: SubjectName)

    @Query("SELECT * FROM SubjectName")
    fun getSubjectName(): LiveData<List<SubjectName>>

    @Query("SELECT * FROM SubjectName")
    suspend fun getAllSubjectNameNonLive(): List<SubjectName>
}