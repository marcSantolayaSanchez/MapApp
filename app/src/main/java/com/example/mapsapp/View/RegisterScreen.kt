package com.example.mapsapp.View

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.mapsapp.viewModel.MapAppViewModel

@Composable
fun RegisterScreen(navController: NavController, myViewModel: MapAppViewModel) {
    var mailUsuario by rememberSaveable {mutableStateOf("")}
    TextField(
        value = mailUsuario,
        modifier = Modifier.fillMaxWidth(),
        onValueChange = {mailUsuario = it}
    )
}