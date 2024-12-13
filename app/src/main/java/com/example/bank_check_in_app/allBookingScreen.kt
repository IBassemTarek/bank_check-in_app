package com.example.bank_check_in_app

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.ui.graphics.Color

 
 
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllBookingsScreen(
    navController: NavController,
    bookingViewModel: BookingViewModel
) {   
    val bookings = bookingViewModel.allBookings.collectAsStateWithLifecycle()
    val isLoading = bookingViewModel.allIsLoading.collectAsStateWithLifecycle()

    var showCancelDialog by remember { mutableStateOf(false) }
    var selectedBookingId by remember { mutableStateOf<String?>(null) }

        // Fetch bookings when screen opens
        LaunchedEffect(Unit) {
            bookingViewModel.fetchAllBookings()
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
        "All Bookings",
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
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