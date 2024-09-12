package com.gdalamin.bcs_pro.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gdalamin.bcs_pro.data.model.SharedData

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