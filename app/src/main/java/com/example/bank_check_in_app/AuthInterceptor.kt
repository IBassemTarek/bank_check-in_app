package com.example.bank_check_in_app

import okhttp3.Interceptor
import okhttp3.Response
import com.example.bank_check_in_app.TokenManager

class AuthInterceptor(private val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val accessToken = tokenManager.getAccessToken()
        
        val authenticatedRequest = if (accessToken != null) {
            request.newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()
        } else request
        
        return chain.proceed(authenticatedRequest)
    }
}