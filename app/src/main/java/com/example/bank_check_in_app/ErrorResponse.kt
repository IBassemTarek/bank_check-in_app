package com.example.bank_check_in_app

data class ErrorResponse(
    val message: String,
    val error: String,
    val statusCode: Int
)