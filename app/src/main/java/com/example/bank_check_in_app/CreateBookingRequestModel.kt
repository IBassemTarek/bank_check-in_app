package com.example.bank_check_in_app

data class CreateBookingRequest(
    val branch_id: String,
    val date: String
)