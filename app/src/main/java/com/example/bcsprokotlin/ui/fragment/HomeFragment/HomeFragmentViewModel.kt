package com.example.bcsprokotlin.ui.fragment.HomeFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bcsprokotlin.repository.Repository
import com.example.bcsprokotlin.ui.fragment.SubjectsFragment.SubjectName
import com.example.bcsprokotlin.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    val _subject: MutableLiveData<MutableList<Resource<SubjectName>>> = MutableLiveData()
    val subject: MutableLiveData<MutableList<Resource<SubjectName>>> = _subject


//    suspend fun
}