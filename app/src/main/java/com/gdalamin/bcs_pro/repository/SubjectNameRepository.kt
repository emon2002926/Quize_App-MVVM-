package com.gdalamin.bcs_pro.repository

import androidx.lifecycle.LiveData
import com.gdalamin.bcs_pro.db.dao.SubjectDao
import com.gdalamin.bcs_pro.model.SubjectName
import javax.inject.Inject

class SubjectNameRepository @Inject constructor(
    private val subjectDao: SubjectDao
) {
    val allSubjectsName: LiveData<List<SubjectName>> = subjectDao.getSubjectName()

    suspend fun insertAll(subjectsName: List<SubjectName>) {
        subjectDao.insertAllSubjectName(subjectsName)
    }

    fun getAllExams(): LiveData<List<SubjectName>> {
        return subjectDao.getSubjectName()
    }

    suspend fun getAllExamsNonLive(): List<SubjectName> {
        return subjectDao.getAllSubjectNameNonLive()
    }

    suspend fun isDatabaseEmpty(): Boolean {
        return subjectDao.getAllSubjectNameNonLive().isEmpty()
    }
}