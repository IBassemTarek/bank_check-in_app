package com.example.bank_check_in_app

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bank_check_in_app.UserViewModel

class BookingViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookingViewModel::class.java)) {
            val apiService = NetworkModule.createApiService(context)
            @Suppress("UNCHECKED_CAST")
            return BookingViewModel(apiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}