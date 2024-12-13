package com.example.bank_check_in_app

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.DELETE
import retrofit2.http.Path
import com.example.bank_check_in_app.UserResponse

data class LoginRequest(
    val national_id: String,
    val password: String
)

data class LoginResponse(
    val access_token: String,
    val refresh_token: String
)

data class RefreshTokenRequest(
    val refresh_token: String
)

interface ApiService {
    @POST("auth")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
    
    @POST("auth/exchange-token")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<LoginResponse>

    @GET("user")
    suspend fun getUser(): Response<UserResponse>

    @GET("booking/today")
    suspend fun getTodayBookings(): Response<List<BookingResponse>>

    @GET("booking")
    suspend fun getAllBookings(): Response<List<BookingResponse>>

    @DELETE("booking/{id}")
    suspend fun cancelBooking(@Path("id") bookingId: String): Response<Unit>

    @GET("branch")
    suspend fun getBranches(): Response<List<BranchResponse>>
}