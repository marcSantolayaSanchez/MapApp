package com.example.mapsapp.View

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mapsapp.Navigation.Routes
import com.example.mapsapp.viewModel.MapAppViewModel

@Composable
fun LoginScreen(navController: NavController, myViewModel: MapAppViewModel) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Button(
            onClick = {
                myViewModel.login(email,password)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue, contentColor = Color.White),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(text = "Login", fontSize = 18.sp)
        }

        Button(
            onClick = {
                navController.navigate(Routes.RegisterScreen.route)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue, contentColor = Color.White),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(text = "Registrate!", fontSize = 18.sp)
        }

        Button(
            onClick = {
                navController.navigate(Routes.MapScreen.route)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue, contentColor = Color.White),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(text = "Mapa", fontSize = 18.sp)
        }
    }
}
