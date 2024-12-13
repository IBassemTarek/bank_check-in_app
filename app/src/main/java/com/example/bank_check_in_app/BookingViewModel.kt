package com.example.bank_check_in_app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookingViewModel(
    private val apiService: ApiService
) : ViewModel() {
    private val _bookings = MutableStateFlow<List<BookingResponse>?>(null)
    val bookings: StateFlow<List<BookingResponse>?> = _bookings.asStateFlow()

    private val _allBookings = MutableStateFlow<List<BookingResponse>?>(null)
    val allBookings: StateFlow<List<BookingResponse>?> = _allBookings.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _allIsLoading = MutableStateFlow(false)
    val allIsLoading: StateFlow<Boolean> = _allIsLoading.asStateFlow()

    fun fetchTodayBookings() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.getTodayBookings()
                if (response.isSuccessful) {
                    _bookings.value = response.body()
                }
            } catch (e: Exception) {
                // Handle error
                println("Error fetching bookings: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchAllBookings() {
        viewModelScope.launch {
            _allIsLoading.value = true
            try {
                val response = apiService.getAllBookings()
                if (response.isSuccessful) {
                    _allBookings.value = response.body()
                }
            } catch (e: Exception) {
                // Handle error
                println("Error fetching all bookings: ${e.message}")
            } finally {
                _allIsLoading.value = false
            }
        }
    }

    

    fun cancelBooking(bookingId: String) {
        viewModelScope.launch {
            try {
                val response = apiService.cancelBooking(bookingId)
                if (response.isSuccessful) {
                    // Refresh bookings after successful cancellation
                    fetchTodayBookings()
                    fetchAllBookings()
                }
            } catch (e: Exception) {
                println("Error cancelling booking: ${e.message}")
            }
        }
    }
}