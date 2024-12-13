package com.example.bank_check_in_app

data class UserResponse(
    val id: String,
    val first_name: String,
    val last_name: String,
    val national_id: String,
    val email: String,
    val phone_number: String,
    val city: City,
    val country: Country,
    val phone_country: PhoneCountry
)

data class City(
    val id: String,
    val name: String,
    val country_id: String
)

data class Country(
    val id: String,
    val name: String,
    val iso_code: String,
    val calling_code: String,
    val flag: String
)

data class PhoneCountry(
    val id: String,
    val name: String,
    val iso_code: String,
    val calling_code: String,
    val flag: String
)