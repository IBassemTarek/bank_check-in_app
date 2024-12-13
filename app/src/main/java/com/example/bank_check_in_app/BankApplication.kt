package com.example.bank_check_in_app

import android.app.Application
import android.content.Context

class BankApplication : Application() {
    // Services and managers
    lateinit var tokenManager: TokenManager
        private set
    
    lateinit var apiService: ApiService
        private set
    
    companion object {
        private lateinit var instance: BankApplication
        
        fun getInstance(): BankApplication = instance
        
        // Helper function to get dependencies from anywhere in the app
        fun getDependencies(): AppDependencies {
            return object : AppDependencies {
                override val tokenManager: TokenManager = instance.tokenManager
                override val apiService: ApiService = instance.apiService
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initializeDependencies()
    }

    private fun initializeDependencies() {
        // Initialize TokenManager
        tokenManager = TokenManager(applicationContext)
        
        // Initialize ApiService using NetworkModule
        apiService = NetworkModule.createApiService(applicationContext)
    }
}

// Interface to expose dependencies
interface AppDependencies {
    val tokenManager: TokenManager
    val apiService: ApiService
}

// Extension function to easily get dependencies from ViewModels or other classes
val Context.appDependencies: AppDependencies
    get() = BankApplication.getDependencies()