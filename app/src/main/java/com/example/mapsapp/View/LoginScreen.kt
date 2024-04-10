package com.example.mapsapp.View

import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.mapsapp.Navigation.Routes
import com.example.mapsapp.viewModel.MapAppViewModel

@Composable
fun LoginScreen(navController: NavController, myViewModel: MapAppViewModel) {
    Button(onClick = { navController.navigate(Routes.RegisterScreen.route) }) {

    }

    Button(onClick = { navController.navigate(Routes.MapsScreen.route) }) {

    }


}