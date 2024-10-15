package com.gdalamin.bcs_pro.data.local.repositories

import androidx.lifecycle.LiveData
import com.gdalamin.bcs_pro.data.local.dao.QuestionBankDao
import com.gdalamin.bcs_pro.data.model.BcsYearName
import javax.inject.Inject

class LocalQuestionBankRepository @Inject constructor(
    private val questionBankDao: QuestionBankDao
) {
    
    suspend fun insertAll(bcsYearName: List<BcsYearName>) {
        questionBankDao.insertAllBcsYearName(bcsYearName)
    }
    
    fun getAllBcsYearName(): LiveData<List<BcsYearName>> {
        return questionBankDao.getBcsYearName()
    }
    
    suspend fun getAllBcsYearNameNonLive(): List<BcsYearName> {
        return questionBankDao.getAllBcsYearNameNonLive()
    }
    
    suspend fun isDatabaseEmpty(): Boolean {
        return questionBankDao.getAllBcsYearNameNonLive().isEmpty()
    }
    
    suspend fun updateIsQuestionSaved(id: Int, isSaved: Boolean) {
        questionBankDao.updateIsQuestionSaved(id, isSaved)
    }
}