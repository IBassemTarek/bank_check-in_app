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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.clickable

 
 
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun newBookingsScreen(
    navController: NavController,
    bookingViewModel: BookingViewModel,
    branchViewModel: BranchViewModel
) {   
    val branches = branchViewModel.branches.collectAsStateWithLifecycle()
    val isLoading = branchViewModel.isLoading.collectAsStateWithLifecycle()
    var selectedBranch by remember { mutableStateOf<BranchResponse?>(null) }
    var expanded by remember { mutableStateOf(false) }
    
        // Fetch bookings when screen opens
        LaunchedEffect(Unit) {
            branchViewModel.fetchBranches()
        }

        Scaffold(
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomHeader(navController,
        "Create New Booking",
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
                        branches.value.isNullOrEmpty() -> {
                            Text(
                                text = "There are no branches available",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                        else -> {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                    value = selectedBranch?.name ?: "Select Branch",
                                    onValueChange = {},
                                    enabled = false,
                                    readOnly = true,
                                    colors =
                                            OutlinedTextFieldDefaults.colors(
                                                    disabledTextColor =
                        MaterialTheme.colorScheme.onSurface,
                                                    disabledContainerColor = Color.Transparent,
                                                    disabledBorderColor =
                        MaterialTheme.colorScheme.outline,
                                                    disabledLeadingIconColor =
                        
                        MaterialTheme.colorScheme.onSurfaceVariant,
                                                    disabledTrailingIconColor =
                                                            MaterialTheme.colorScheme.onSurface,
                                                    disabledLabelColor =
                        
                        MaterialTheme.colorScheme.onSurfaceVariant,
                                                    disabledPlaceholderColor =
                        
                        MaterialTheme.colorScheme.onSurfaceVariant,
                                                    disabledSupportingTextColor =
                        
                        MaterialTheme.colorScheme.onSurfaceVariant,
                                                    disabledPrefixColor =
                        
                        MaterialTheme.colorScheme.onSurfaceVariant,
                                                    disabledSuffixColor =
                        
                        MaterialTheme.colorScheme.onSurfaceVariant
                                            ),
                                    label = { Text("Branch") },
                                    trailingIcon = {
                                        Icon(
                                                imageVector = Icons.Filled.ArrowDropDown,
                                                contentDescription = "Dropdown Icon"
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth().clickable { expanded =
                        true }
                            )

                            DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    modifier = Modifier.fillMaxWidth().padding(horizontal =
                        16.dp)
                            ) {
                                branches.value?.let { branchList ->
                                    branchList.forEach { branch ->
                                        DropdownMenuItem(
                                            onClick = {
                                                selectedBranch = branch
                                                expanded = false
                                            },
                                            text = { 
                                                Column {
                                                    Text(branch.name)
                                                    Text(
                                                        text = "${branch.city.name}, ${branch.country.name}",
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                                    )
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }
                        }
                    }
                }
            }
        
        }
    }
}