package com.example.bcsprokotlin.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bcsprokotlin.model.SharedData

class SharedViewModel : ViewModel() {
    private val _sharedData = MutableLiveData<SharedData>()
    val sharedData: LiveData<SharedData> get() = _sharedData

    private val _sharedString = MutableLiveData<String>()
    val sharedString: LiveData<String> get() = _sharedString


    fun setSharedData(data: SharedData) {
        _sharedData.value = data
    }

    fun setStringData(data: String) {
        _sharedString.value = data
    }


}