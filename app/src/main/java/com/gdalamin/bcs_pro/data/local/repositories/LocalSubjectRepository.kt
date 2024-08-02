package com.gdalamin.bcs_pro.data.local.repositories

import androidx.lifecycle.LiveData
import com.gdalamin.bcs_pro.data.local.dao.SubjectDao
import com.gdalamin.bcs_pro.data.model.SubjectName
import javax.inject.Inject

class LocalSubjectRepository @Inject constructor(
    private val subjectDao: SubjectDao
) {

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
