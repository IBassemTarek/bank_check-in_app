package com.example.bank_check_in_app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.bank_check_in_app.ApiService

class UserViewModel : ViewModel() {
    private val _userData = MutableStateFlow<UserResponse?>(null)
    val userData: StateFlow<UserResponse?> = _userData.asStateFlow()

    fun setUserData(user: UserResponse) {
        _userData.value = user
        println("Debug: Setting user data - ${user.first_name}")
    }
}