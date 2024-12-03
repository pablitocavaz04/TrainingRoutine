package com.example.trainingroutine_pablocavaz.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _userRole = MutableLiveData<String>()
    val userRole: LiveData<String> get() = _userRole

    fun setUserRole(role: String) {
        _userRole.value = role
    }
}