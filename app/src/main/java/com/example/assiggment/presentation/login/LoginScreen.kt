package com.example.assiggment.presentation.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state = viewModel.state

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (state.isLoginSuccess) {
                Text(text = "Login Successful!", style = MaterialTheme.typography.headlineMedium)
            } else {
                Text(text = "Peanut Login", style = MaterialTheme.typography.headlineMedium)
                
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = state.login,
                    onValueChange = { viewModel.onLoginChanged(it) },
                    label = { Text("Login (ID)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = state.password,
                    onValueChange = { viewModel.onPasswordChanged(it) },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (state.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Button(
                        onClick = { viewModel.onLoginClick() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Login")
                    }
                }

                if (state.error != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = state.error, color = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}
