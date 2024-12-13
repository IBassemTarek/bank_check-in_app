package com.example.bank_check_in_app

data class BranchResponse(
    val id: String,
    val name: String,
    val swift_code: String,
    val city: City,
    val country: Country,
    val created_at: String
)