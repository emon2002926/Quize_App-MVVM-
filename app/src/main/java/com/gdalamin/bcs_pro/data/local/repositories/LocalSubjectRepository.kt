package com.gdalamin.bcs_pro.data.local.repositories

import com.gdalamin.bcs_pro.data.local.dao.SubjectDao
import com.gdalamin.bcs_pro.data.model.SubjectName
import javax.inject.Inject

class LocalSubjectRepository @Inject constructor(
    private val subjectDao: SubjectDao
) {
    
    suspend fun insertAll(subjectsName: List<SubjectName>) {
        subjectDao.insertAllSubjectName(subjectsName)
    }
    
    suspend fun getAllSubjectFromDB(): List<SubjectName> {
        return subjectDao.getAllSubjectFromDB()
    }
    
    suspend fun isDatabaseEmpty(): Boolean {
        return subjectDao.getAllSubjectFromDB().isEmpty()
    }
}
