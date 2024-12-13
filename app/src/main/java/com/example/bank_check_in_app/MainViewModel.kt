package com.example.bank_check_in_app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.bank_check_in_app.ApiService
import com.example.bank_check_in_app.UserViewModel

class MainViewModel(
    private val apiService: ApiService,
    private val tokenManager: TokenManager,
    private val userViewModel: UserViewModel 
) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()
    
    fun login(nationalId: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val loginResponse = apiService.login(LoginRequest(nationalId, password))
                if (loginResponse.isSuccessful) {
                    loginResponse.body()?.let { loginData ->
                        // Save tokens
                        tokenManager.saveTokens(
                            loginData.access_token,
                            loginData.refresh_token
                        )
                        
                        // Fetch user data
                        try {
                            val userResponse = apiService.getUser()
                            if (userResponse.isSuccessful) {
                                userResponse.body()?.let { userData ->
                                    // Add debug log
                                    println("Setting user data: ${userData.first_name}")
                                    userViewModel.setUserData(userData)
                                    _loginState.value = LoginState.Success
                                }
                            } else {
                                handleErrorResponse(userResponse.code(), userResponse.message())
                            }
                        } catch (e: Exception) {
                            handleException(e)
                        }
                    }
                } else {
                    handleErrorResponse(loginResponse.code(), loginResponse.message())
                }
            } catch (e: Exception) {
                handleException(e)
            }
        }
    }
    
    private fun handleErrorResponse(code: Int, message: String) {
        val errorMessage = when (code) {
            401 -> "Invalid credentials"
            403 -> "Access denied"
            404 -> "Service not found"
            500 -> "Server error, please try again later"
            else -> "Error: $message"
        }
        _loginState.value = LoginState.Error(errorMessage)
    }
    
    private fun handleException(e: Exception) {
        val errorMessage = when (e) {
            is java.net.UnknownHostException -> "No internet connection"
            is java.net.SocketTimeoutException -> "Connection timed out"
            is retrofit2.HttpException -> "Server error: ${e.message()}"
            else -> "Network error: ${e.message}"
        }
        _loginState.value = LoginState.Error(errorMessage)
    }

   // Update LoginState to not include user data
   sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        object Success : LoginState()
        data class Error(val message: String) : LoginState()
    }
}
