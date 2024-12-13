package com.example.bank_check_in_app

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bank_check_in_app.UserViewModel

class MainViewModelFactory(
    private val context: Context,
    private val userViewModel: UserViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            val tokenManager = TokenManager(context)
            val apiService = NetworkModule.createApiService(context)
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(apiService, tokenManager, userViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}