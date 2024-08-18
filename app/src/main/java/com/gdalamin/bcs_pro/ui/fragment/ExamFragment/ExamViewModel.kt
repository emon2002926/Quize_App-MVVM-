package com.gdalamin.bcs_pro.ui.fragment.ExamFragment

import androidx.lifecycle.ViewModel
import com.gdalamin.bcs_pro.data.remote.repositories.ExamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExamViewModel @Inject constructor(private val repository: ExamRepository) : ViewModel() {


}