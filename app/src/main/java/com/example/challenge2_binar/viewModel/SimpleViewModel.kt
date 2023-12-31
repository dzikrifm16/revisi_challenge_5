package com.example.challenge2_binar.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.challenge2_binar.api.APIService
import com.example.challenge2_binar.util.Resource
import kotlinx.coroutines.Dispatchers

class SimpleViewModel (private val apiService: APIService) : ViewModel() {
    fun getAllCategory() = liveData(Dispatchers.IO) {
        try {
            emit(Resource.success( apiService.getList()))
        } catch (exception: Exception) {
            emit(Resource.error(null,exception.message ?: "Error Occurred!"))
        }
    }
}