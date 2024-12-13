package com.example.bank_check_in_app

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import kotlinx.coroutines.runBlocking
import com.example.bank_check_in_app.TokenManager

class AuthAuthenticator(
    private val tokenManager: TokenManager,
    private val apiService: ApiService
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = tokenManager.getRefreshToken() ?: return null
        
        return runBlocking {
            try {
                val refreshResponse = apiService.refreshToken(RefreshTokenRequest(refreshToken))
                if (refreshResponse.isSuccessful) {
                    val newTokens = refreshResponse.body()!!
                    tokenManager.saveTokens(newTokens.access_token, newTokens.refresh_token)
                    
                    response.request
                        .newBuilder()
                        .header("Authorization", "Bearer ${newTokens.access_token}")
                        .build()
                } else {
                    tokenManager.clearTokens()
                    null
                }
            } catch (e: Exception) {
                tokenManager.clearTokens()
                null
            }
        }
    }
}