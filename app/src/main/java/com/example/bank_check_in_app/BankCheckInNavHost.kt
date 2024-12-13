package com.example.bank_check_in_app

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner

@Composable
fun BankCheckInNavHost() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // Create shared UserViewModel
    val userViewModel: UserViewModel = viewModel(viewModelStoreOwner = LocalViewModelStoreOwner.current!!)
    val bookingViewModel: BookingViewModel = viewModel(
        factory = BookingViewModelFactory(context)
    )
    val branchViewModel: BranchViewModel = viewModel(
        factory = BranchViewModelFactory(context)
    )

    NavHost(navController = navController, startDestination = "main_screen") {
        composable("main_screen") {
            MainScreen(
                navController = navController,
                viewModel = viewModel(
                    factory = MainViewModelFactory(LocalContext.current, userViewModel)
                )
            )
        }
        composable("second_screen") {
            SecondScreen(
                navController = navController,
                userViewModel = userViewModel,
                bookingViewModel = bookingViewModel
            )
        }
        composable("all_bookings_screen") {
            AllBookingsScreen(
                navController = navController,
                bookingViewModel = bookingViewModel
            )
        }
        composable("new_booking_screen") {
            newBookingsScreen(
                navController = navController,
                bookingViewModel = bookingViewModel,
                branchViewModel = branchViewModel
            )
        }
    }
}