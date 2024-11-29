package com.example.bank_check_in_app

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.shape.CircleShape
import com.example.bank_check_in_app.R.drawable.female_profile_image
import com.example.bank_check_in_app.R.drawable.male_profile_image
import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale


@Composable
fun CustomHeader(navController: NavController) {
    Surface(
        color = MaterialTheme.colorScheme.primary, // Use primary color
        shadowElevation = 4.dp // Shadow effect
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp), // Padding within the header
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back button
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White // Set icon color to white for contrast
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Header Title
            Text(
                text = "User Details",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.White // Set title color to white for contrast
            )
        }
    }
}

@Composable
fun SecondScreen(
    firstName: String,
    lastName: String,
    gender: String,
    customersTillTurn: Int,
    navController: NavController
) {

    val profileImage = if (gender == "Female") {
        female_profile_image // Replace with actual resource
    } else {
        male_profile_image // Replace with actual resource
    }

    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomHeader(navController)
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally // Center content horizontally
        ) {
            // Top Row with image and name
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .graphicsLayer {
                            shape = CircleShape
                            clip = true
                        }
                ) {
                    Image(
                        painter = painterResource(id = profileImage),
                        contentDescription = "Profile Image",
                        modifier = Modifier.fillMaxSize(), // Make the image fill the Box
                        contentScale = ContentScale.Crop // Ensure the image scales to cover the circle
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Column for Name and Gender
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(text = "$firstName $lastName", fontSize = 18.sp , fontWeight = FontWeight.Bold,)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Gender: $gender", fontSize = 18.sp)
                }
            }

            // Centered Text: Customers till your turn
            Text(
                text = "Customers till your turn: $customersTillTurn",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SecondScreenPreview() {
    SecondScreen(
        firstName = "Rahma",
        lastName = "Abdelkhalek",
        gender = "Female",
        customersTillTurn = 5,
        navController = rememberNavController()
    )
}
