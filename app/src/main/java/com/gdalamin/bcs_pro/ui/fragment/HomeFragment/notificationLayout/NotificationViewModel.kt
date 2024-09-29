package com.gdalamin.bcs_pro.ui.fragment.HomeFragment.notificationLayout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gdalamin.bcs_pro.data.model.UserNotification
import com.gdalamin.bcs_pro.data.remote.repositories.NotificationRepository
import com.gdalamin.bcs_pro.utilities.Constants.Companion.CHECK_INTERNET_CONNECTION_MESSAGE
import com.gdalamin.bcs_pro.utilities.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    
    private val notificationRepository: NotificationRepository
) : ViewModel() {
    private val pageNumber = 1
    
    private val _notification: MutableLiveData<DataState<MutableList<UserNotification>>> =
        MutableLiveData()
    val notification: LiveData<DataState<MutableList<UserNotification>>> = _notification
    
    private var isDataLoaded = false
    
    
    fun getNotification() {
        viewModelScope.launch {
            when (isDataLoaded) {
                true -> {}
                false -> {
                    _notification.postValue(DataState.Loading())
                    try {
                        val response = notificationRepository.getNotification(pageNumber)
                        when (response.isSuccessful) {
                            true -> {
                                _notification.postValue(DataState.Success(response.body()!!));
                                isDataLoaded = true
                            }
                            
                            false -> _notification.postValue(DataState.Error(response.message()))
                        }
                    } catch (t: Throwable) {
                        when (t) {
                            is IOException -> _notification.postValue(
                                DataState.Error(
                                    CHECK_INTERNET_CONNECTION_MESSAGE
                                )
                            )
                        }
                    }
                }
            }
        }
    }
    
    
}