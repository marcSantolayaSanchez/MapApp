package com.example.mapsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.mapsapp.ui.theme.MapsAppTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mapsapp.Navigation.Routes
import com.example.mapsapp.View.CamaraScreen
import com.example.mapsapp.View.GalleryScreen
import com.example.mapsapp.View.LoginScreen
import com.example.mapsapp.View.MapScreen
import com.example.mapsapp.View.MyDrawer
import com.example.mapsapp.View.RegisterScreen
import com.example.mapsapp.viewModel.MapAppViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val myViewModel by viewModels<MapAppViewModel>()
            MapsAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyDrawer(myViewModel = MapAppViewModel() )
                }
            }
        }
    }
}

