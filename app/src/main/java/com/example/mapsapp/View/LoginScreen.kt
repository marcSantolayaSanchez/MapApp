package com.example.mapsapp.View

import Model.UserPrefs
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mapsapp.Navigation.Routes
import com.example.mapsapp.viewModel.MapAppViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun LoginScreen(navController: NavController, myViewModel: MapAppViewModel) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val chequearLogin by myViewModel.goToNext.observeAsState(false)
    var isPressed by rememberSaveable{ mutableStateOf(false)}
    if (chequearLogin){
        navController.navigate(Routes.MapScreen.route)
    }
    val context = LocalContext.current
    val userPrefs = UserPrefs(context)
    val storedUserData = userPrefs.getUserData.collectAsState(initial = emptyList())
     if (storedUserData.value.isNotEmpty() && storedUserData.value[0] != ""
        && storedUserData.value[1] != "" && storedUserData.value[2] != ""){
        email = storedUserData.value[0]
        password = storedUserData.value[1]
        if (storedUserData.value[2] == "true"){
            myViewModel.login(storedUserData.value[0], storedUserData.value[1])
        }

    }


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
        Row (modifier = Modifier .padding(20.dp), horizontalArrangement = Arrangement.SpaceBetween){
            Checkbox(checked = isPressed , onCheckedChange = {isPressed = it})
            Text(text = "Guardar datos", style = TextStyle(fontSize = 25.sp, fontStyle = FontStyle.Italic))
        }
        

        Button(
            onClick = {
                if (email.isBlank() || password.isBlank()) {
                    Toast.makeText(context, "Pon todos los campos.", Toast.LENGTH_SHORT).show()
                } else if (!email.endsWith("@gmail.com")) {
                    Toast.makeText(context, "Tiene que ser correo que termine en @gmail.com.", Toast.LENGTH_SHORT).show()
                } else {
                    myViewModel.login(email, password)
                    if (isPressed) {
                        CoroutineScope(Dispatchers.IO).launch {
                            userPrefs.saveUserData(email, password, "true")
                        }
                    } else {
                        CoroutineScope(Dispatchers.IO).launch {
                            userPrefs.saveUserData("", "", "false")
                        }
                    }
                }

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
    }
}
