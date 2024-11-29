package com.example.bank_check_in_app

import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun SnackbarHost(snackbarHostState: SnackbarHostState) {
    // Custom Snackbar with color customization
    SnackbarHost(
        hostState = snackbarHostState,
        modifier = Modifier.padding(16.dp),
        snackbar = { snackbarData ->
            Snackbar(
                snackbarData = snackbarData,
                containerColor = Color.Red, 
                contentColor = Color.White,  
            )
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    var phoneNumber by remember { mutableStateOf("") }
    var isValid by remember { mutableStateOf(true) }
    var selectedBranch by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) } // Controls dropdown visibility
    // Snackbar state
    val snackbarHostState = remember { SnackbarHostState() }
    var showSnackbar by remember { mutableStateOf(false) }

    val firstName = "Rahma"
    val lastName = "Abdelkhalek"
    val gender = "Female"
    val customersTillTurn = 1

    val branchOptions = listOf(
        "Alexandria branch",
        "Kafr Abdo branch",
        "Smouha branch"
    )


    LaunchedEffect(showSnackbar) {
        if (showSnackbar) {
            snackbarHostState.showSnackbar(
                message = "Failed to check in. Please try again.",
                duration = SnackbarDuration.Short
            )
            showSnackbar = false // Reset the flag after showing the snackbar
        }
    }


    Scaffold(
    snackbarHost = { SnackbarHost(snackbarHostState) }, // Pass the SnackbarHostState here
    modifier = Modifier.fillMaxSize()
) { _ ->
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo), // Replace with your asset name
            contentDescription = "App Logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Phone Number Row
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Egyptian Flag
            Image(
                painter = painterResource(id = R.drawable.egypt_flag), // Replace with your drawable
                contentDescription = "Egypt Flag",
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Country Code
            Text(
                text = "+20",
                fontSize = 18.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Phone Number Input
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { input ->
                    // Allow only numbers and limit to 10 digits
                    if (input.length <= 10 && input.all { it.isDigit() }) {
                        phoneNumber = input
                    }
                    isValid = phoneNumber.length == 10 // Update validation status
                },
                isError = !isValid,
                placeholder = { Text("Phone Number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Validation Error Message
        if (!isValid) {
            Text(
                text = "Phone number must be exactly 10 digits.",
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        
         // Dropdown for Branch Selection
         Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            // OutlinedTextField for branch, that opens dropdown when clicked
            OutlinedTextField(
                value = selectedBranch.ifEmpty { "Select Branch" },
                onValueChange = {},
                enabled = false,
                readOnly = true,
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledContainerColor = Color.Transparent,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledPrefixColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledSuffixColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                label = { Text("Branch") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = "Dropdown Icon"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true } // Open dropdown on click anywhere in OutlinedTextField
            )

            // Dropdown menu that expands when clicked
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth() // Set dropdown menu width to full screen
                    .padding(horizontal = 16.dp) // Optional padding for aesthetics
            ) {
                branchOptions.forEach { branch ->
                    DropdownMenuItem(
                        onClick = {
                            selectedBranch = branch
                            expanded = false
                        },
                        text = { Text(branch) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Submit Button
        Button(
            onClick = {
                if (phoneNumber.length == 10 && selectedBranch.isNotEmpty()) {
                    // Handle valid phone number and branch submission
                     showSnackbar = true
                    // navController.navigate("second_screen/${firstName}/${lastName}/${gender}/${customersTillTurn}")
                } else {
                     isValid = phoneNumber.length == 10 // Ensure validation
                }
            },
            modifier = Modifier.fillMaxWidth(),
             enabled = phoneNumber.length == 10 && selectedBranch.isNotEmpty() // Button is enabled only if both conditions are met
        ) {
            Text("Check In")
        }
    }
}
}

