package com.example.bank_check_in_app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.bank_check_in_app.ApiService
import com.example.bank_check_in_app.UserViewModel

class BranchViewModel(
    private val apiService: ApiService
) : ViewModel() {
    private val _branches = MutableStateFlow<List<BranchResponse>?>(null)
    val branches: StateFlow<List<BranchResponse>?> = _branches.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        fetchBranches()
    }

    fun fetchBranches() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = apiService.getBranches()
                if (response.isSuccessful) {
                    _branches.value = response.body()
                }
            } catch (e: Exception) {
                println("Error fetching branches: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}