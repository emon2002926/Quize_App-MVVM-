package com.gdalamin.bcs_pro.data.repository

import androidx.lifecycle.LiveData
import com.gdalamin.bcs_pro.data.local.dao.ExamInfoDao
import com.gdalamin.bcs_pro.data.model.LiveExam
import javax.inject.Inject

class ExamInfoRepository @Inject constructor(
    private val examInfoDao: ExamInfoDao
) {
    val allExamInfo: LiveData<List<LiveExam>> = examInfoDao.getExamInfo()

    suspend fun insertAll(exams: List<LiveExam>) {
        examInfoDao.insertAll(exams)
    }

    fun getAllExams(): LiveData<List<LiveExam>> {
        return examInfoDao.getExamInfo()
    }

    suspend fun getAllExamsNonLive(): List<LiveExam> {
        return examInfoDao.getAllExamsNonLive()
    }

    suspend fun isDatabaseEmpty(): Boolean {
        return examInfoDao.getAllExamsNonLive().isEmpty()
    }
}