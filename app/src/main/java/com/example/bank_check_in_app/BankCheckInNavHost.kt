package com.example.bank_check_in_app

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument


@Composable
fun BankCheckInNavHost() {
    val navController = rememberNavController() // Create NavController

    // NavHost to manage navigation between screens
    NavHost(navController = navController, startDestination = "main_screen") {
        composable("main_screen") {
            MainScreen(navController = navController)
        }
        composable(
            "second_screen/{firstName}/{lastName}/{gender}/{customersTillTurn}",
            arguments = listOf(
                navArgument("firstName") { type = NavType.StringType },
                navArgument("lastName") { type = NavType.StringType },
                navArgument("gender") { type = NavType.StringType },
                navArgument("customersTillTurn") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val firstName = backStackEntry.arguments?.getString("firstName") ?: ""
            val lastName = backStackEntry.arguments?.getString("lastName") ?: ""
            val gender = backStackEntry.arguments?.getString("gender") ?: ""
            val customersTillTurn = backStackEntry.arguments?.getInt("customersTillTurn") ?: 0

            SecondScreen(firstName, lastName, gender, customersTillTurn , navController)
        }
    }
}
