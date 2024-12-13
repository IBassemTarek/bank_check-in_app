package com.example.bank_check_in_app

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bank_check_in_app.MainViewModel.LoginState
import kotlinx.coroutines.launch

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
fun MainScreen(navController: NavController, viewModel: MainViewModel) {
        var nationalID by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var isValidNationalID by remember { mutableStateOf(true) }
        var isValidPassword by remember { mutableStateOf(true) }
        var passwordVisible by remember { mutableStateOf(false) }


        val loginState by viewModel.loginState.collectAsState()
        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        LaunchedEffect(loginState) {
                when (loginState) {
                        is LoginState.Success -> {
                                navController.navigate("second_screen") {
                                        popUpTo("main_screen") { inclusive = true }
                                }
                        }
                        is LoginState.Error -> {
                                scope.launch {
                                        snackbarHostState.showSnackbar(
                                                message = (loginState as LoginState.Error).message,
                                                duration = SnackbarDuration.Long,
                                                // Add action to dismiss or retry
                                                withDismissAction = true
                                        )
                                }
                        }
                        else -> {}
                }
        }

        Scaffold(
                snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState) { data ->
                                Snackbar(
                                        snackbarData = data,
                                        containerColor = MaterialTheme.colorScheme.errorContainer,
                                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                                )
                        }
                },
                modifier = Modifier.fillMaxSize()
        ) { _ ->
                Column(
                        modifier = Modifier.fillMaxSize().padding(16.dp),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                ) {
                        Image(
                                painter = painterResource(id = R.drawable.logo),
                                contentDescription = "App Logo",
                                modifier = Modifier.fillMaxWidth().height(150.dp)
                        )
                        Spacer(modifier = Modifier.height(32.dp))

                        OutlinedTextField(
                                value = nationalID,
                                onValueChange = { input ->
                                        if (input.length <= 14 && input.all { it.isDigit() }) {
                                                nationalID = input
                                        }
                                        isValidNationalID =
                                                nationalID.length == 14 // Update validation status
                                },
                                isError = !isValidNationalID,
                                placeholder = { Text("National ID") },
                                keyboardOptions =
                                        KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth()
                        )

                        if (!isValidNationalID) {
                                Text(
                                        text = "National ID must be exactly 14 digits.",
                                        color = Color.Red,
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(top = 8.dp)
                                )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                                value = password,
                                onValueChange = { input ->
                                        password = input
                                        val hasUpperCase = password.any { it.isUpperCase() }
                                        val hasLowerCase = password.any { it.isLowerCase() }
                                        val hasDigit = password.any { it.isDigit() }
                                        val isCorrectLength = password.length >= 9

                                        isValidPassword =
                                                hasUpperCase &&
                                                        hasLowerCase &&
                                                        hasDigit &&
                                                        isCorrectLength
                                },
                                isError = !isValidPassword,
                                placeholder = { Text("Password") },
                                visualTransformation = if (passwordVisible) {
                                        VisualTransformation.None
                                    } else {
                                        PasswordVisualTransformation()
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                trailingIcon = {
                                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                        Icon(
                                            painter = painterResource(
                                                id = if (passwordVisible) {
                                                        R.drawable.baseline_visibility_off_24
                                                } else {
                                                        R.drawable.baseline_visibility_24 
                                                }
                                            ),
                                            contentDescription = if (passwordVisible) {
                                                "Hide password"
                                            } else {
                                                "Show password"
                                            }
                                        )
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                        )

                        if (!isValidPassword) {
                                Text(
                                        text =
                                                "Password must be 9 characters with uppercase, lowercase, and a number.",
                                        color = Color.Red,
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(top = 8.dp)
                                )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Spacer(modifier = Modifier.height(32.dp))

                        Button(
                                onClick = {
                                        if (isValidNationalID && isValidPassword) {
                                                viewModel.login(nationalID, password)
                                        }
                                },
                                modifier = Modifier.fillMaxWidth().height(56.dp),
                                shape = RoundedCornerShape(4.dp),
                                enabled =
                                        nationalID.length == 14 &&
                                                password.any { it.isUpperCase() } &&
                                                password.any { it.isLowerCase() } &&
                                                password.any { it.isDigit() } &&
                                                password.length >= 9 &&
                                                loginState !is LoginState.Loading
                        ) {
                                if (loginState is LoginState.Loading) {
                                        CircularProgressIndicator(
                                                modifier = Modifier.size(24.dp),
                                                color = MaterialTheme.colorScheme.onPrimary
                                        )
                                } else {
                                        Text("Login")
                                }
                        }
                }
        }
}
