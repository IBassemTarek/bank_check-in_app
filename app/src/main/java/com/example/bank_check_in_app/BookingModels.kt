package com.example.bank_check_in_app

data class BookingResponse(
    val id: String,
    val date: String,
    val status: String,
    val branch: Branch,
    val remaining_bookings_count: Int?,
    val created_at: String,
)

data class Branch(
    val id: String,
    val name: String,
    val swift_code: String
)
