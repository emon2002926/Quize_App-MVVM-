package com.gdalamin.bcs_pro.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.gdalamin.bcs_pro.data.model.BcsYearName

@Dao
interface QuestionBankDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBcsYearName(subject: BcsYearName)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllBcsYearName(exams: List<BcsYearName>)
    
    @Update
    suspend fun updateBcsYearName(bcsYearName: List<BcsYearName>)
    
    @Query("SELECT * FROM BcsYearName")
    fun getBcsYearName(): LiveData<List<BcsYearName>>
    
    @Query("SELECT * FROM BcsYearName")
    suspend fun getAllBcsYearNameNonLive(): List<BcsYearName>
    
    @Query("DELETE FROM BcsYearName")
    suspend fun deleteAll()
}