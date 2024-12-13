package com.example.bank_check_in_app

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.size
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Delete
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold


enum class BookingStatus(val value: String, val color: Color) {
    PENDING("PENDING", Color(0xFFFFA000)),      // Orange
    IN_PROGRESS("IN_PROGRESS", Color(0xFF2196F3)), // Blue
    CANCELLED("CANCELLED", Color(0xFFE53935)),    // Red
    COMPLETED("COMPLETED", Color(0xFF4CAF50));    // Green

    companion object {
        fun fromString(value: String): BookingStatus =
            values().find { it.value == value } ?: PENDING
    }
}

// Helper function to format date
  fun formatDate(dateString: String): String {
    return try {
        val instant = Instant.parse(dateString)
        val localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()
        localDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
    } catch (e: Exception) {
        dateString
    }
}

// Helper function for simple date format (YYYY-MM-DD)
  fun formatDateSimple(dateString: String): String {
    return try {
        val instant = Instant.parse(dateString)
        val localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()
        localDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))
    } catch (e: Exception) {
        dateString
    }
}



@Composable
fun CustomHeader(
    navController: NavController, 
    title: String,
    currentRoute: String = navController.currentDestination?.route ?: ""
) {
    Surface(
        color = MaterialTheme.colorScheme.primary, 
        shadowElevation = 4.dp 
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp), 
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
           
            if (currentRoute != "second_screen") {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.White 
            )
        }
    }
}


@Composable
fun BookingItem(booking: BookingResponse ,onCancelClick: (String) -> Unit) {
    val status = BookingStatus.fromString(booking.status)
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Branch: ${booking.branch.name}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))

                        // Booking date
                        Text(
                            text = "Date: ${formatDateSimple(booking.date)}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    
                    // Format and display the date
                    Text(
                        text = "Booked since: ${formatDate(booking.created_at)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                // Status chip
                Surface(
                    color = status.color.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = status.value.replace("_", " "),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        color = status.color,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
            // Display remaining bookings count with an icon
            if (booking.remaining_bookings_count != null && booking.remaining_bookings_count > 0) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "People ahead",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${booking.remaining_bookings_count} people ahead of you",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(0.dp))
            }
            if (booking.status == "PENDING") {
                IconButton(
                    onClick = { onCancelClick(booking.id) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Cancel booking",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel(),
    bookingViewModel: BookingViewModel
) {   
    val userData = userViewModel.userData.collectAsStateWithLifecycle()
    val bookings = bookingViewModel.bookings.collectAsStateWithLifecycle()
    val isLoading = bookingViewModel.isLoading.collectAsStateWithLifecycle()

    var showCancelDialog by remember { mutableStateOf(false) }
    var selectedBookingId by remember { mutableStateOf<String?>(null) }

        // Fetch bookings when screen opens
        LaunchedEffect(Unit) {
            bookingViewModel.fetchTodayBookings()
        }

        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate("new_booking_screen") },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add new booking"
                    )
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomHeader(navController,
         "User Details",
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        userData.value?.let { user ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "${user.first_name} ${user.last_name}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(text = "Email: ${user.email}")
                    Text(text = "Phone: ${user.phone_country.calling_code}${user.phone_number}")
                    Text(text = "City: ${user.city.name}, ${user.country.name}")
                    Text(text = "National ID: ${user.national_id}")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Today's Bookings Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                    Text(
                        text = "Today's Bookings",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    TextButton(
                        onClick = { navController.navigate("all_bookings_screen") }
                    ) {
                        Text(
                            text = "See all bookings",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                    when {
                        isLoading.value -> {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(16.dp)
                            )
                        }
                        bookings.value.isNullOrEmpty() -> {
                            Text(
                                text = "No bookings for today",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                        else -> {
                            bookings.value?.forEach { booking ->
                                BookingItem(booking, 
                                onCancelClick = { bookingId ->
                                    selectedBookingId = bookingId
                                    showCancelDialog = true
                                }
                                )
                                Divider(modifier = Modifier.padding(vertical = 8.dp))
                            }
                        }
                    }
                }
            }
        } ?: run {
            CircularProgressIndicator(
                modifier = Modifier.padding(top = 32.dp)
            )
        }
    }
    }
        // Confirmation Dialog
        if (showCancelDialog) {
            AlertDialog(
                onDismissRequest = { 
                    showCancelDialog = false
                    selectedBookingId = null 
                },
                title = { Text("Cancel Booking") },
                text = { Text("Are you sure you want to cancel this booking?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            selectedBookingId?.let { bookingViewModel.cancelBooking(it) }
                            showCancelDialog = false
                            selectedBookingId = null
                        }
                    ) {
                        Text("Yes", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { 
                            showCancelDialog = false
                            selectedBookingId = null
                        }
                    ) {
                        Text("No")
                    }
                }
            )
        }
}