package com.gdalamin.bcs_pro.data.local.repositories

import com.gdalamin.bcs_pro.data.local.dao.ExamInfoDao
import com.gdalamin.bcs_pro.data.model.LiveExam
import javax.inject.Inject

class LocalExamInfoRepository @Inject constructor(
    private val examInfoDao: ExamInfoDao
) {
    
    suspend fun insertAll(exams: List<LiveExam>) {
        examInfoDao.insertAll(exams)
    }
    
    
    suspend fun getAllExamsNonLive(): List<LiveExam> {
        return examInfoDao.getAllExamsNonLive()
    }
    
    suspend fun isDatabaseEmpty(): Boolean {
        return examInfoDao.getAllExamsNonLive().isEmpty()
    }
    
    suspend fun deleteAllExamInfo() {
        return examInfoDao.deleteAll()
    }
}
