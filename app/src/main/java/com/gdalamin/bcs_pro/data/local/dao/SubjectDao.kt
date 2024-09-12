package com.gdalamin.bcs_pro.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.gdalamin.bcs_pro.data.model.SubjectName

@Dao
interface SubjectDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSubjectName(subject: SubjectName)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSubjectName(exams: List<SubjectName>)
    
    @Update
    suspend fun updateSubjectName(examInfo: SubjectName)

//    @Query("SELECT * FROM SubjectName")
//    fun getSubjectName(): LiveData<List<SubjectName>>
    
    @Query("SELECT * FROM SubjectName")
    suspend fun getAllSubjectFromDB(): List<SubjectName>
}